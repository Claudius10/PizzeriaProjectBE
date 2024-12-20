package org.pizzeria.api.web.exceptions;

import lombok.AllArgsConstructor;
import org.pizzeria.api.entity.error.Error;
import org.pizzeria.api.repos.error.ErrorRepository;
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
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String CLASS_NAME_SHORT = "G.E.H";

	private final ErrorRepository errorRepository;

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatusCode status,
			WebRequest request
	) {

		List<String> errorMessages = new ArrayList<>();

		ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
			errorMessages.add(String.format("Field: %s - Error: %s - Value: %s",
					fieldError.getField(),
					fieldError.getDefaultMessage(),
					fieldError.getRejectedValue()));
		});

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.BAD_REQUEST.name())
						.code(HttpStatus.BAD_REQUEST.value())
						.build())
				.error(Error.builder()
						.cause(ex.getClass().getSimpleName())
						.message(String.valueOf(errorMessages))
						.origin(CLASS_NAME_SHORT + ".handleMethodArgumentNotValid")
						.path(((ServletWebRequest) request).getRequest().getServletPath())
						.logged(false)
						.fatal(false)
						.build())
				.build();

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler({DataAccessException.class})
	protected ResponseEntity<Response> dataAccessException(DataAccessException ex, WebRequest request) {

		boolean uniqueEmailConstraint = ex instanceof DataIntegrityViolationException && ex.getMessage().contains("constraint [USER_EMAIL]");

		Error error = Error.builder()
				.cause(uniqueEmailConstraint ? ApiResponses.USER_EMAIL_ALREADY_EXISTS : ex.getClass().getSimpleName())
				.origin(CLASS_NAME_SHORT + ".dataAccessException")
				.path(((ServletWebRequest) request).getRequest().getServletPath())
				.message(uniqueEmailConstraint ? null : ex.getMessage())
				.logged(!uniqueEmailConstraint)
				.fatal(!uniqueEmailConstraint)
				.build();

		if (!uniqueEmailConstraint) {
			errorRepository.save(error);
		}

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.BAD_REQUEST.name())
						.code(HttpStatus.BAD_REQUEST.value())
						.build())
				.error(error)
				.build();

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<Response> accessDeniedException(AccessDeniedException ex, WebRequest request) {

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.UNAUTHORIZED.name())
						.code(HttpStatus.UNAUTHORIZED.value())
						.build())
				.error(Error.builder()
						.cause(ex.getClass().getSimpleName())
						.message(ex.getMessage())
						.origin(CLASS_NAME_SHORT  + ".accessDeniedException")
						.path(((ServletWebRequest) request).getRequest().getServletPath())
						.logged(false)
						.fatal(false)
						.build())
				.build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(AuthenticationException.class)
	protected ResponseEntity<Response> authenticationException(AuthenticationException ex, WebRequest request) {

		String errorMessage = ex instanceof BadCredentialsException ? SecurityResponses.BAD_CREDENTIALS : ex.getMessage();

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.UNAUTHORIZED.name())
						.code(HttpStatus.UNAUTHORIZED.value())
						.build())
				.error(Error.builder()
						.cause(ex.getClass().getSimpleName())
						.message(errorMessage)
						.origin(CLASS_NAME_SHORT  + ".authenticationException")
						.path(((ServletWebRequest) request).getRequest().getServletPath())
						.logged(false)
						.fatal(false)
						.build())
				.build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Response> unknownException(Exception ex, WebRequest request) {

		Error error = Error.builder()
				.cause(ex.getClass().getSimpleName())
				.message(ex.getMessage())
				.origin(CLASS_NAME_SHORT  + ".unknownException")
				.path(((ServletWebRequest) request).getRequest().getServletPath())
				.logged(true)
				.fatal(true)
				.build();

		errorRepository.save(error);

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.INTERNAL_SERVER_ERROR.name())
						.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
						.build())
				.error(error)
				.build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}