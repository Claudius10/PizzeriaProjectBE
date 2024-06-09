package PizzaApp.api.exceptions;

import PizzaApp.api.utils.globals.SecurityResponses;
import PizzaApp.api.utils.globals.ValidationResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
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

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

	@ExceptionHandler({ConstraintViolationException.class})
	protected ResponseEntity<String> handleEntityFieldExceptions(HttpServletRequest request, ConstraintViolationException ex) {

		Set<ConstraintViolation<?>> violationSet = ex.getConstraintViolations();
		List<String> errorMessages = new ArrayList<>();

		for (ConstraintViolation<?> violation : violationSet) {
			errorMessages.add(violation.getMessage());
		}

		return ResponseEntity.badRequest().body(String.valueOf(errorMessages));
	}

	@ExceptionHandler({SQLIntegrityConstraintViolationException.class})
	protected ResponseEntity<String> handleDuplicateDatabaseEntry(HttpServletRequest request, SQLIntegrityConstraintViolationException ex) {
		String message = ex.getMessage();
		if (ex.getErrorCode() == 1062) {
			message = String.valueOf(ex.getErrorCode());
		}

		return ResponseEntity.badRequest().body((message));
	}

	@ExceptionHandler({AuthenticationException.class, AccessDeniedException.class})
	protected ResponseEntity<String> handleAuthenticationExceptions(HttpServletRequest request, RuntimeException ex) {

		String errorMsg;
		if (ex instanceof BadCredentialsException) {
			errorMsg = SecurityResponses.BAD_CREDENTIALS;
		} else {
			errorMsg = ex.getMessage();
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMsg);
	}

	@ExceptionHandler({DataIntegrityViolationException.class})
	protected ResponseEntity<String> handleDataAccessExceptions(HttpServletRequest request, RuntimeException ex) {
		// TODO - test whatever returning static message in all cases is a good idea
		return ResponseEntity.badRequest().body(ValidationResponses.EMAIL_ALREADY_EXISTS);
	}
}