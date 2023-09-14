package PizzaApp.api.exceptions;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import PizzaApp.api.exceptions.errorDTO.ApiErrorDTO;
import PizzaApp.api.exceptions.exceptions.order.*;
import PizzaApp.api.exceptions.exceptions.user.MaxAddressListSizeException;
import PizzaApp.api.exceptions.exceptions.user.MaxTelListSizeException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
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
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		return ResponseEntity
				.status(status)
				.body(new ApiErrorDTO.Builder(
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")))
						.withStatusCode(status.value())
						.withPath(request.getDescription(false))
						.withErrors(
								ex.getBindingResult()
										.getFieldErrors()
										.stream()
										.map(DefaultMessageSourceResolvable::getDefaultMessage)
										.collect(Collectors.toList()))
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
						.withErrors(List.of(ex.getMessage()))
						.build());
	}

	@ExceptionHandler({
			ConstraintViolationException.class,
			InvalidContactTelephoneException.class,
			BlankEmailException.class})
	protected ResponseEntity<ApiErrorDTO> handleEntityFieldExceptions(HttpServletRequest request, RuntimeException ex) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ApiErrorDTO.Builder(
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")))
						.withStatusCode(HttpStatus.BAD_REQUEST.value())
						.withPath(request.getServletPath())
						.withErrors(List.of(ex.getMessage()))
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
						.withErrors(List.of(ex.getMessage()))
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
						.withErrors(List.of(ex.getMessage()))
						.build());
	}

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	protected ResponseEntity<ApiErrorDTO> handleUniqueUsernameException(HttpServletRequest request) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ApiErrorDTO.Builder(
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")))
						.withStatusCode(HttpStatus.BAD_REQUEST.value())
						.withPath(request.getServletPath())
						.withErrors(List.of(
								"Una cuenta con el email introducido ya existe. " +
										"Si no recuerda la contraseña, pulse \"Restablecer contraseña.\""))
						.build());
	}
}