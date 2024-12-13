package org.pizzeria.api.web.exceptions;

import org.pizzeria.api.web.dto.api.Response;
import org.pizzeria.api.web.exceptions.custom.RoleNotFoundException;
import org.pizzeria.api.web.globals.ApiResponses;
import org.pizzeria.api.web.globals.SecurityResponses;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
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
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatusCode status,
			WebRequest request
	) {

		List<String> errorMessages = new ArrayList<>();

		ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
			errorMessages.add(String.format("Error: %s - Value: %s", fieldError.getDefaultMessage(), fieldError.getRejectedValue()));
		});

		Response response = Response.builder()
				.statusDescription(HttpStatus.BAD_REQUEST.name())
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.errorClass(ex.getClass().getSimpleName())
				.errorMessage(String.valueOf(errorMessages))
				.build();

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler({DataAccessException.class})
	protected ResponseEntity<Response> handleDataAccessExceptions(DataAccessException ex) {

		Response response = Response.builder()
				.statusDescription(HttpStatus.BAD_REQUEST.name())
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.errorClass(ex.getClass().getSimpleName())
				.errorMessage(ex instanceof DataIntegrityViolationException ? ApiResponses.USER_EMAIL_ALREADY_EXISTS : ex.getMessage())
				.build();

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<Response> handleAuthenticationExceptions(AccessDeniedException ex) {

		Response response = Response.builder()
				.statusDescription(HttpStatus.BAD_REQUEST.name())
				.statusCode(HttpStatus.UNAUTHORIZED.value())
				.errorClass(ex.getClass().getSimpleName())
				.errorMessage(ex.getMessage())
				.build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(AuthenticationException.class)
	protected ResponseEntity<Response> handleAuthenticationExceptions(AuthenticationException ex) {

		String errorMessage = ex instanceof BadCredentialsException ? SecurityResponses.BAD_CREDENTIALS : ex.toString();

		Response response = Response.builder()
				.statusDescription(HttpStatus.BAD_REQUEST.name())
				.statusCode(HttpStatus.UNAUTHORIZED.value())
				.errorClass(ex.getClass().getSimpleName())
				.errorMessage(errorMessage)
				.build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(RoleNotFoundException.class)
	protected ResponseEntity<Response> handleRoleNotFoundException(RoleNotFoundException ex) {

		Response response = Response.builder()
				.statusDescription(HttpStatus.BAD_REQUEST.name())
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.errorClass(ex.getClass().getSimpleName())
				.errorMessage(ex.getMessage())
				.build();

		return ResponseEntity.internalServerError().body(response);
	}
}