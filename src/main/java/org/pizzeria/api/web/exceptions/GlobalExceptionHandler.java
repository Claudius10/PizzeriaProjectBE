package org.pizzeria.api.web.exceptions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pizzeria.api.entity.error.Error;
import org.pizzeria.api.repos.error.ErrorRepository;
import org.pizzeria.api.web.dto.api.Response;
import org.pizzeria.api.web.dto.api.Status;
import org.pizzeria.api.web.globals.ApiResponses;
import org.pizzeria.api.web.globals.SecurityResponses;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String CLASS_NAME_SHORT = "G.E.H";
	private static final String INFO_LOG = "Exception caught -> {}";

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
						.isError(true)
						.build())
				.error(Error.builder()
						.id(UUID.randomUUID().getMostSignificantBits())
						.cause(ex.getClass().getSimpleName())
						.message(String.valueOf(errorMessages))
						.origin(CLASS_NAME_SHORT + ".handleMethodArgumentNotValid")
						.path(((ServletWebRequest) request).getRequest().getServletPath())
						.logged(false)
						.fatal(false)
						.build())
				.build();

		log.info(INFO_LOG, response);
		return ResponseEntity.status(HttpStatus.OK).body(response); // return OK to get the ResponseDTO in onSuccess callback
	}

	@ExceptionHandler({DataAccessException.class})
	protected ResponseEntity<Response> dataAccessException(DataAccessException ex, WebRequest request) {

		boolean fatal = true;

		if (ex instanceof DataIntegrityViolationException && ex.getMessage() != null && ex.getMessage().contains("constraint [USER_EMAIL]")) {
			fatal = false;
		} else if (ex instanceof DataRetrievalFailureException && ApiResponses.ORDER_NOT_FOUND.equals(ex.getMessage())) {
			fatal = false;
		}

		Error error = Error.builder()
				.id(UUID.randomUUID().getMostSignificantBits())
				.cause(!fatal ? ApiResponses.USER_EMAIL_ALREADY_EXISTS : ex.getClass().getSimpleName())
				.origin(CLASS_NAME_SHORT + ".dataAccessException")
				.path(((ServletWebRequest) request).getRequest().getServletPath())
				.message(!fatal ? null : ex.getMessage())
				.logged(fatal)
				.fatal(fatal)
				.build();

		if (fatal) {
			error.setId(null);
			Error savedError = errorRepository.save(error);
			error.setId(savedError.getId());
		}

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.BAD_REQUEST.name())
						.code(HttpStatus.BAD_REQUEST.value())
						.isError(true)
						.build())
				.error(error)
				.build();

		log.info(INFO_LOG, response);
		return ResponseEntity.status(HttpStatus.OK).body(response); // return OK to get the ResponseDTO in onSuccess callback
	}

	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<Response> accessDeniedException(AccessDeniedException ex, WebRequest request) {

		Error error = Error.builder()
				.id(UUID.randomUUID().getMostSignificantBits())
				.cause(ex.getClass().getSimpleName())
				.message(ex.getMessage())
				.origin(CLASS_NAME_SHORT + ".accessDeniedException")
				.path(((ServletWebRequest) request).getRequest().getServletPath())
				.logged(false)
				.fatal(true)
				.build();

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.UNAUTHORIZED.name())
						.code(HttpStatus.UNAUTHORIZED.value())
						.isError(true)
						.build())
				.error(error)
				.build();

		log.info(INFO_LOG, response);
		return ResponseEntity.status(HttpStatus.OK).body(response); // return OK to get the ResponseDTO in onSuccess callback
	}

	@ExceptionHandler(AuthenticationException.class)
	protected ResponseEntity<Response> authenticationException(AuthenticationException ex, WebRequest request) {

		String errorMessage;
		boolean fatal = false;

		switch (ex) {
			case BadCredentialsException ignored -> errorMessage = SecurityResponses.BAD_CREDENTIALS;
			case UsernameNotFoundException ignored -> errorMessage = SecurityResponses.USER_NOT_FOUND;
			case InvalidBearerTokenException ignored -> errorMessage = SecurityResponses.INVALID_TOKEN;
			default -> {
				fatal = true;
				errorMessage = ex.getMessage();
			}
		}

		Error error = Error.builder()
				.id(UUID.randomUUID().getMostSignificantBits())
				.cause(ex.getClass().getSimpleName())
				.message(errorMessage)
				.origin(CLASS_NAME_SHORT + ".authenticationException")
				.path(((ServletWebRequest) request).getRequest().getServletPath())
				.logged(fatal)
				.fatal(fatal)
				.build();

		if (fatal) {
			error.setId(null);
			Error savedError = this.errorRepository.save(error);
			error.setId(savedError.getId());
		}

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.UNAUTHORIZED.name())
						.code(HttpStatus.UNAUTHORIZED.value())
						.isError(true)
						.build())
				.error(error)
				.build();

		log.info(INFO_LOG, response);
		return ResponseEntity.status(HttpStatus.OK).body(response); // return OK to get the ResponseDTO in onSuccess callback
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Response> unknownException(Exception ex, WebRequest request) {

		Error error = Error.builder()
				.cause(ex.getClass().getSimpleName())
				.message(ex.getMessage())
				.origin(CLASS_NAME_SHORT + ".unknownException")
				.path(((ServletWebRequest) request).getRequest().getServletPath())
				.logged(true)
				.fatal(true)
				.build();

		errorRepository.save(error);

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.INTERNAL_SERVER_ERROR.name())
						.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
						.isError(true)
						.build())
				.error(error)
				.build();

		log.info(INFO_LOG, response);
		return ResponseEntity.status(HttpStatus.OK).body(response); // return OK to get the ResponseDTO in onSuccess callback
	}
}