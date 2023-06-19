package PizzaApp.api.exceptions;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import PizzaApp.api.exceptions.errorDTO.ApiErrorDTO;
import PizzaApp.api.exceptions.exceptions.*;
import PizzaApp.api.exceptions.exceptions.order.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		return new ResponseEntity<>(
				new ApiErrorDTO.Builder(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")))
						.withStatusCode(status.value())
						.withPath(request.getDescription(false))
						.withErrors(
								ex.getBindingResult()
										.getFieldErrors()
										.stream()
										.map(DefaultMessageSourceResolvable::getDefaultMessage)
										.collect(Collectors.toList()))
						.build(), status);
	}

	@ExceptionHandler({SQLIntegrityConstraintViolationException.class})
	protected ResponseEntity<ApiErrorDTO> handleUniqueUsername(HttpServletRequest request, RuntimeException ex) {
		return new ResponseEntity<>(
				new ApiErrorDTO.Builder(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")))
						.withStatusCode(HttpStatus.BAD_REQUEST.value())
						.withPath(request.getServletPath())
						.withErrors(List.of(
								"Una cuenta con el email introducido ya existe. " +
										"Si no recuerda la contraseña, pulse \"Restablecer contraseña.\""))
						.build(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({
			EmptyCartException.class,
			OrderDataUpdateTimeLimitException.class,
			OrderDeleteTimeLimitException.class,
			StoreNotOpenException.class,
			InvalidChangeRequestedException.class,})
	protected ResponseEntity<ApiErrorDTO> handleCreateOrUpdateOrderExceptions(HttpServletRequest request, RuntimeException ex) {
		return new ResponseEntity<>(
				new ApiErrorDTO.Builder(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")))
						.withStatusCode(HttpStatus.BAD_REQUEST.value())
						.withPath(request.getServletPath())
						.withErrors(List.of(ex.getMessage()))
						.build(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({
			ConstraintViolationException.class,
			InvalidContactTelephoneException.class,
			BlankEmailException.class})
	protected ResponseEntity<ApiErrorDTO> handleEntityFieldExceptions(HttpServletRequest request, RuntimeException ex) {
		return new ResponseEntity<>(
				new ApiErrorDTO.Builder(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")))
						.withStatusCode(HttpStatus.BAD_REQUEST.value())
						.withPath(request.getServletPath())
						.withErrors(List.of(ex.getMessage()))
						.build(), HttpStatus.BAD_REQUEST);
	}
}