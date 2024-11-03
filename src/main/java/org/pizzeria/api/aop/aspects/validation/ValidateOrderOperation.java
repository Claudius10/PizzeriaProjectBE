package org.pizzeria.api.aop.aspects.validation;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.pizzeria.api.entity.order.dto.NewAnonOrderDTO;
import org.pizzeria.api.entity.order.dto.NewUserOrderDTO;
import org.pizzeria.api.entity.order.dto.UpdateUserOrderDTO;
import org.pizzeria.api.validation.order.OrderValidationResult;
import org.pizzeria.api.validation.order.OrderValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ValidateOrderOperation {

	private final OrderValidator orderValidator;

	public ValidateOrderOperation(OrderValidator orderValidator) {
		this.orderValidator = orderValidator;
	}

	@Around(value = "execution(* org.pizzeria.api.controllers.open.AnonController.createAnonOrder(..)) && args(newAnonOrder, request)", argNames = "pjp,newAnonOrder,request")
	public Object validateNewAnonOrder(ProceedingJoinPoint pjp, NewAnonOrderDTO newAnonOrder, HttpServletRequest request) throws Throwable {
		OrderValidationResult result = orderValidator.validate(newAnonOrder.cart(), newAnonOrder.orderDetails());

		if (result.isValid()) {
			return pjp.proceed();
		}

		return ResponseEntity.badRequest().body(result.getMessage());
	}

	@Around(value = "execution(* org.pizzeria.api.controllers.locked.user.UserOrdersController.createUserOrder(..)) && args" +
			"(order, userId, request)", argNames = "pjp,order,userId,request")
	public Object validateNewUserOrder(ProceedingJoinPoint pjp, NewUserOrderDTO order, Long userId, HttpServletRequest request) throws Throwable {
		OrderValidationResult result = orderValidator.validate(order.cart(), order.orderDetails());

		if (result.isValid()) {
			return pjp.proceed();
		}

		return ResponseEntity.badRequest().body(result.getMessage());
	}

	@Around(value = "execution(* org.pizzeria.api.controllers.locked.user.UserOrdersController.updateUserOrder(..)) && args" +
			"(orderId, order, userId, request)", argNames = "pjp,orderId,order,userId,request")
	public Object validateUserOrderUpdate(ProceedingJoinPoint pjp, Long orderId, UpdateUserOrderDTO order, Long userId, HttpServletRequest request) throws Throwable {
		OrderValidationResult result = orderValidator.validateUpdate(order.getCart(), order.getOrderDetails(), order.getCreatedOn());

		if (result.isValid() && result.isCartUpdateValid()) {
			return pjp.proceed();
		}

		if (result.isValid() && !result.isCartUpdateValid()) {
			order.setCart(null);
			return pjp.proceed();
		}

		return ResponseEntity.badRequest().body(result.getMessage());
	}

	@Around(value = "execution(* org.pizzeria.api.controllers.locked.user.UserOrdersController.deleteUserOrderById(..)) && args" +
			"(orderId,userId,request)", argNames = "pjp,orderId,userId,request")
	public Object validateUserOrderDelete(ProceedingJoinPoint pjp, Long orderId, Long userId, HttpServletRequest request) throws Throwable {
		OrderValidationResult result = orderValidator.validateDelete(orderId);

		if (result.isValid()) {
			return pjp.proceed();
		}

		return ResponseEntity.badRequest().body(result.getMessage());
	}
}