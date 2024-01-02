package PizzaApp.api.exceptions;

import PizzaApp.api.exceptions.errorDTO.ApiErrorDTO;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid
			(MethodArgumentNotValidException ex,
			 HttpHeaders headers,
			 HttpStatusCode status,
			 WebRequest request) {
		return ResponseEntity
				.status(status)
				.body(new ApiErrorDTO.Builder()
						.withStatusCode(status.value())
						.withPath(request.getDescription(false))
						.withErrorMsg(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage())
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