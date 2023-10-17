package PizzaApp.api.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import PizzaApp.api.exceptions.errorDTO.ApiErrorDTO;
import PizzaApp.api.exceptions.exceptions.order.*;
import PizzaApp.api.exceptions.exceptions.user.MaxAddressListSizeException;
import PizzaApp.api.exceptions.exceptions.user.MaxTelListSizeException;
import PizzaApp.api.exceptions.exceptions.user.NonUniqueEmailException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
				.body(new ApiErrorDTO.Builder(
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")))
						.withStatusCode(status.value())
						.withPath(request.getDescription(false))
						.withErrorMsg(ex.getMessage())
						.build());
	}

	@ExceptionHandler({
			EmptyCartException.class,
			OrderDataUpdateTimeLimitException.class,
			OrderDeleteTimeLimitException.class,
			StoreNotOpenException.class,
			InvalidChangeRequestedException.class,})
	protected ResponseEntity<ApiErrorDTO> handleCreateOrUpdateOrderExceptions(HttpServletRequest request, RuntimeException ex) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ApiErrorDTO.Builder(
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")))
						.withStatusCode(HttpStatus.BAD_REQUEST.value())
						.withPath(request.getServletPath())
						.withErrorMsg(ex.getMessage())
						.build());
	}

	@ExceptionHandler({ConstraintViolationException.class})
	protected ResponseEntity<ApiErrorDTO> handleEntityFieldExceptions(HttpServletRequest request, RuntimeException ex) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ApiErrorDTO.Builder(
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")))
						.withStatusCode(HttpStatus.BAD_REQUEST.value())
						.withPath(request.getServletPath())
						.withErrorMsg(ex.getMessage())
						.build());
	}

	@ExceptionHandler({MaxTelListSizeException.class, MaxAddressListSizeException.class})
	protected ResponseEntity<ApiErrorDTO> handleUserDataException(HttpServletRequest request, RuntimeException ex) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ApiErrorDTO.Builder(
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")))
						.withStatusCode(HttpStatus.BAD_REQUEST.value())
						.withPath(request.getServletPath())
						.withErrorMsg(ex.getMessage())
						.build());
	}

	@ExceptionHandler(NonUniqueEmailException.class)
	protected ResponseEntity<ApiErrorDTO> handleNonUniqueUsernameException(HttpServletRequest request, RuntimeException ex) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ApiErrorDTO.Builder(
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")))
						.withStatusCode(HttpStatus.BAD_REQUEST.value())
						.withPath(request.getServletPath())
						.withErrorMsg(ex.getMessage())
						.build());
	}

	@ExceptionHandler({AuthenticationException.class, AccessDeniedException.class})
	protected ResponseEntity<ApiErrorDTO> handleAuthenticationExceptions(HttpServletRequest request, RuntimeException ex) {
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(new ApiErrorDTO.Builder(
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")))
						.withStatusCode(HttpStatus.UNAUTHORIZED.value())
						.withPath(request.getServletPath())
						.withErrorMsg(ex.getMessage())
						.build());

	}

	@ExceptionHandler({ExpiredTokenException.class})
	protected ResponseEntity<ApiErrorDTO> handleTokenExceptions(HttpServletRequest request, RuntimeException ex) {
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(new ApiErrorDTO.Builder(
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")))
						.withStatusCode(HttpStatus.UNAUTHORIZED.value())
						.withPath(request.getServletPath())
						.withErrorMsg(ex.getMessage())
						.build());

	}
}