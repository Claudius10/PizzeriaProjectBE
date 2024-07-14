package org.pizzeria.api.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.pizzeria.api.utils.globals.ApiResponses;
import org.pizzeria.api.utils.globals.SecurityResponses;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
																  HttpStatusCode status, WebRequest request) {
		List<String> errorMessages = new ArrayList<>();

		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errorMessages.add(String.format("Error: %s Valor introducido: %s", error.getDefaultMessage(), error.getRejectedValue()));
		}

		return ResponseEntity.badRequest().body(String.valueOf(errorMessages));
	}

	@ExceptionHandler({DataIntegrityViolationException.class})
	protected ResponseEntity<String> handleDataAccessExceptions(HttpServletRequest request, DataIntegrityViolationException ex) {

		ConstraintViolationException cve = (ConstraintViolationException) ex.getCause();
		String constraint = cve.getConstraintName();
		String message = cve.getMessage();

		if (constraint != null) {
			message = switch (constraint) {
				case "UK_OB8KQYQQGMEFL0ACO34AKDTPE" -> ApiResponses.EMAIL_ALREADY_EXISTS;
				case "UK_M3NRR354U2L0HE9RIUB37LTQN" -> ApiResponses.NUMBER_ALREADY_EXISTS;
				default -> message;
			};
		}

		return ResponseEntity.badRequest().body(message);
	}

	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<String> handleAuthenticationExceptions(HttpServletRequest request, AccessDeniedException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}

	@ExceptionHandler(AuthenticationException.class)
	protected ResponseEntity<String> handleAuthenticationExceptions(HttpServletRequest request, AuthenticationException ex) {

		String errorMsg;
		if (ex instanceof BadCredentialsException) {
			errorMsg = SecurityResponses.BAD_CREDENTIALS;
		} else {
			errorMsg = ex.getMessage();
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMsg);
	}
}