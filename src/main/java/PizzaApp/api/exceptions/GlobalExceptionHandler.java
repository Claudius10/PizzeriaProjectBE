package PizzaApp.api.exceptions;

import PizzaApp.api.entity.dto.error.ApiErrorDTO;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid
			(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<String> errorMessages = new ArrayList<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errorMessages.add("Error del atributo " + "'" + error.getField() + "' con input: '" + error.getRejectedValue() +
					"'. Razón: " + error.getDefaultMessage());
		}

		return ResponseEntity
				.status(status)
				.body(new ApiErrorDTO.Builder()
						.withStatusCode(status.value())
						.withPath(request.getDescription(false))
						.withErrorMsg(String.valueOf(errorMessages))
						.build());
	}

	@ExceptionHandler({ConstraintViolationException.class})
	protected ResponseEntity<ApiErrorDTO> handleEntityFieldExceptions(HttpServletRequest request, RuntimeException ex) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ApiErrorDTO.Builder()
						.withStatusCode(HttpStatus.BAD_REQUEST.value())
						.withPath(request.getServletPath())
						.withErrorMsg(ex.getMessage())
						.build());
	}

	@ExceptionHandler({SQLIntegrityConstraintViolationException.class})
	protected ResponseEntity<ApiErrorDTO> handleDuplicateDatabaseEntry(HttpServletRequest request, SQLIntegrityConstraintViolationException ex) {
		String message = ex.getMessage();
		if (ex.getErrorCode() == 1062) {
			message = String.valueOf(ex.getErrorCode());
		}

		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ApiErrorDTO.Builder()
						.withStatusCode(HttpStatus.BAD_REQUEST.value())
						.withPath(request.getServletPath())
						.withErrorMsg(message)
						.build());
	}

	@ExceptionHandler({AuthenticationException.class, AccessDeniedException.class})
	protected ResponseEntity<ApiErrorDTO> handleAuthenticationExceptions(HttpServletRequest request, RuntimeException ex) {

		String errorMsg;
		if (ex instanceof BadCredentialsException) {
			errorMsg = "Email o contraseña incorrecta";
		} else {
			errorMsg = ex.getMessage();
		}

		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(new ApiErrorDTO.Builder()
						.withStatusCode(HttpStatus.UNAUTHORIZED.value())
						.withPath(request.getServletPath())
						.withErrorMsg(errorMsg)
						.build());
	}
}