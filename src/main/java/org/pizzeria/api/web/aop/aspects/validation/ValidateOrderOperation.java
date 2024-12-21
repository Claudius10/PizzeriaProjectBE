package org.pizzeria.api.web.aop.aspects.validation;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.pizzeria.api.entity.error.Error;
import org.pizzeria.api.web.dto.api.Response;
import org.pizzeria.api.web.dto.api.Status;
import org.pizzeria.api.web.dto.order.dto.NewAnonOrderDTO;
import org.pizzeria.api.web.dto.order.dto.NewUserOrderDTO;
import org.pizzeria.api.web.dto.order.dto.UpdateUserOrderDTO;
import org.pizzeria.api.web.order.validation.OrderValidationResult;
import org.pizzeria.api.web.order.validation.OrderValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Aspect
public class ValidateOrderOperation {

	private final OrderValidator orderValidator;

	public ValidateOrderOperation(OrderValidator orderValidator) {
		this.orderValidator = orderValidator;
	}

	@Around(value = "execution(* org.pizzeria.api.web.controllers.open.AnonController.createAnonOrder(..)) && args(newAnonOrder, request)", argNames = "pjp,newAnonOrder,request")
	public Object validateNewAnonOrder(ProceedingJoinPoint pjp, NewAnonOrderDTO newAnonOrder, HttpServletRequest request) throws Throwable {
		OrderValidationResult result = orderValidator.validate(newAnonOrder.cart(), newAnonOrder.orderDetails());

		if (result.isValid()) {
			return pjp.proceed();
		}

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.BAD_REQUEST.name())
						.code(HttpStatus.BAD_REQUEST.value())
						.isError(true)
						.build())
				.error(Error.builder()
						.id(UUID.randomUUID().getMostSignificantBits())
						.cause(result.getMessage())
						.origin(ValidateOrderOperation.class.getSimpleName() + ".validateNewAnonOrder")
						.path(request.getPathInfo())
						.logged(false)
						.fatal(false)
						.build())
				.build();

		return ResponseEntity.badRequest().body(response);
	}

	@Around(value = "execution(* org.pizzeria.api.web.controllers.user.UserOrdersController.createUserOrder(..)) && args" +
			"(order, userId, request)", argNames = "pjp,order,userId,request")
	public Object validateNewUserOrder(ProceedingJoinPoint pjp, NewUserOrderDTO order, Long userId, HttpServletRequest request) throws Throwable {
		OrderValidationResult result = orderValidator.validate(order.cart(), order.orderDetails());

		if (result.isValid()) {
			return pjp.proceed();
		}

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.BAD_REQUEST.name())
						.code(HttpStatus.BAD_REQUEST.value())
						.isError(true)
						.build())
				.error(Error.builder()
						.id(UUID.randomUUID().getMostSignificantBits())
						.cause(result.getMessage())
						.origin(ValidateOrderOperation.class.getSimpleName() + ".validateNewUserOrder")
						.path(request.getPathInfo())
						.logged(false)
						.fatal(false)
						.build())
				.build();

		return ResponseEntity.badRequest().body(response);
	}

	@Around(value = "execution(* org.pizzeria.api.web.controllers.user.UserOrdersController.updateUserOrder(..)) && args" +
			"(orderId, order, userId, request)", argNames = "pjp,orderId,order,userId,request")
	public Object validateUserOrderUpdate(ProceedingJoinPoint pjp, Long orderId, UpdateUserOrderDTO order, Long userId, HttpServletRequest request) throws Throwable {
		OrderValidationResult result = orderValidator.validateUpdate(order.cart(), order.orderDetails(), order.createdOn());

		if (result.isValid() && result.isCartUpdateValid()) {
			return pjp.proceed();
		}

		if (result.isValid() && !result.isCartUpdateValid()) {
			UpdateUserOrderDTO updateUserOrderDTO = order.withCart(null);
			return pjp.proceed(new Object[]{orderId, updateUserOrderDTO, userId, request});
		}

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.BAD_REQUEST.name())
						.code(HttpStatus.BAD_REQUEST.value())
						.isError(true)
						.build())
				.error(Error.builder()
						.id(UUID.randomUUID().getMostSignificantBits())
						.cause(result.getMessage())
						.origin(ValidateOrderOperation.class.getSimpleName() + ".validateUserOrderUpdate")
						.path(request.getPathInfo())
						.logged(false)
						.fatal(false)
						.build())
				.build();

		return ResponseEntity.badRequest().body(response);
	}

	@Around(value = "execution(* org.pizzeria.api.web.controllers.user.UserOrdersController.deleteUserOrderById(..)) && args" +
			"(orderId,userId,request)", argNames = "pjp,orderId,userId,request")
	public Object validateUserOrderDelete(ProceedingJoinPoint pjp, Long orderId, Long userId, HttpServletRequest request) throws Throwable {
		OrderValidationResult result = orderValidator.validateDelete(orderId);

		if (result.isValid()) {
			return pjp.proceed();
		}

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.BAD_REQUEST.name())
						.code(HttpStatus.BAD_REQUEST.value())
						.isError(true)
						.build())
				.error(Error.builder()
						.id(UUID.randomUUID().getMostSignificantBits())
						.cause(result.getMessage())
						.origin(ValidateOrderOperation.class.getSimpleName() + ".validateUserOrderDelete")
						.path(request.getPathInfo())
						.logged(false)
						.fatal(false)
						.build())
				.build();

		return ResponseEntity.badRequest().body(response);
	}
}