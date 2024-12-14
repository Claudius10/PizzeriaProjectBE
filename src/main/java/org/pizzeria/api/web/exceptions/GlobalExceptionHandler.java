package org.pizzeria.api.web.exceptions;

import org.pizzeria.api.web.dto.api.ApiError;
import org.pizzeria.api.web.dto.api.Response;
import org.pizzeria.api.web.dto.api.Status;
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
				.status(Status.builder()
						.description(HttpStatus.BAD_REQUEST.name())
						.code(HttpStatus.BAD_REQUEST.value())
						.build())
				.error(ApiError.builder()
						.cause(ex.getClass().getSimpleName())
						.message(String.valueOf(errorMessages))
						.origin(GlobalExceptionHandler.class.getSimpleName() + ".handleMethodArgumentNotValid")
						.logged(false)
						.build())
				.build();

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler({DataAccessException.class})
	protected ResponseEntity<Response> dataAccessException(DataAccessException ex) {

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.BAD_REQUEST.name())
						.code(HttpStatus.BAD_REQUEST.value())
						.build())
				.error(ApiError.builder()
						.cause(ex instanceof DataIntegrityViolationException ? ApiResponses.USER_EMAIL_ALREADY_EXISTS : ex.getClass().getSimpleName())
						.message(ex.getMessage())
						.origin(GlobalExceptionHandler.class.getSimpleName() + ".dataAccessException")
						.logged(false)
						.build())
				.build();

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<Response> accessDenied(AccessDeniedException ex) {

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.UNAUTHORIZED.name())
						.code(HttpStatus.UNAUTHORIZED.value())
						.build())
				.error(ApiError.builder()
						.cause(ex.getClass().getSimpleName())
						.message(ex.getMessage())
						.origin(GlobalExceptionHandler.class.getSimpleName() + ".accessDenied")
						.logged(false)
						.build())
				.build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(AuthenticationException.class)
	protected ResponseEntity<Response> authenticationException(AuthenticationException ex) {

		String errorMessage = ex instanceof BadCredentialsException ? SecurityResponses.BAD_CREDENTIALS : ex.getMessage();

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.UNAUTHORIZED.name())
						.code(HttpStatus.UNAUTHORIZED.value())
						.build())
				.error(ApiError.builder()
						.cause(ex.getClass().getSimpleName())
						.message(errorMessage)
						.origin(GlobalExceptionHandler.class.getSimpleName() + ".authenticationException")
						.logged(false)
						.build())
				.build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}
}