package PizzaApp.api.validation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import PizzaApp.api.validation.errorDTO.ApiErrorDTO;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import PizzaApp.api.validation.exceptions.EmptyCartException;
import PizzaApp.api.validation.exceptions.InvalidChangeRequestedException;
import PizzaApp.api.validation.exceptions.InvalidContactTelephoneException;
import PizzaApp.api.validation.exceptions.OrderDataUpdateTimeLimitException;
import PizzaApp.api.validation.exceptions.OrderDeleteTimeLimitException;
import PizzaApp.api.validation.exceptions.StoreNotOpenException;
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

	@ExceptionHandler(NoResultException.class)
	protected ResponseEntity<ApiErrorDTO> handleNoResult(HttpServletRequest request, NoResultException ex) {

		return new ResponseEntity<>(
				new ApiErrorDTO.Builder(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")))
						.withStatusCode(HttpStatus.NOT_FOUND.value())
						.withPath(request.getServletPath())
						.withErrors(List.of(ex.getMessage()))
						.build(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ConstraintViolationException.class,
			InvalidChangeRequestedException.class,
			EmptyCartException.class,
			InvalidContactTelephoneException.class,
			OrderDataUpdateTimeLimitException.class,
			OrderDeleteTimeLimitException.class,
			StoreNotOpenException.class})
	protected ResponseEntity<ApiErrorDTO> handleConstraintViolationException(HttpServletRequest request, RuntimeException ex) {
		return new ResponseEntity<>(
				new ApiErrorDTO.Builder(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")))
						.withStatusCode(HttpStatus.BAD_REQUEST.value())
						.withPath(request.getServletPath())
						.withErrors(List.of(ex.getMessage()))
						.build(), HttpStatus.BAD_REQUEST);
	}
}