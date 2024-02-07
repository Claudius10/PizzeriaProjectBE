package PizzaApp.api.aop.validation;

import PizzaApp.api.entity.dto.error.ApiErrorDTO;
import PizzaApp.api.entity.dto.order.UpdateUserOrderDTO;
import PizzaApp.api.entity.dto.order.UserOrderDTO;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.validation.order.OrderValidationResult;
import PizzaApp.api.validation.order.OrderValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Aspect
public class ValidateOrderOperation {

	private final OrderValidator orderValidator;

	public ValidateOrderOperation(OrderValidator orderValidator) {
		this.orderValidator = orderValidator;
	}

	@Around(value = "execution(* PizzaApp.api.controllers.open.AnonController.createAnonOrder(..)) && args(order, request)", argNames = "pjp,order,request")
	public Object validateNewAnonOrder(ProceedingJoinPoint pjp, Order order, HttpServletRequest request) throws Throwable {
		OrderValidationResult result = orderValidator.validate(order);

		if (result.isValid()) {
			return pjp.proceed();
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ApiErrorDTO.Builder()
						.withStatusCode(HttpStatus.BAD_REQUEST.value())
						.withPath(request.getServletPath())
						.withErrorMsg(result.getMessage())
						.build());
	}

	@Around(value = "execution(* PizzaApp.api.controllers.locked.UserOrdersController.createUserOrder(..)) && args(order, request)", argNames = "pjp,order,request")
	public Object validateNewUserOrder(ProceedingJoinPoint pjp, UserOrderDTO order, HttpServletRequest request) throws Throwable {
		Order theOrder = new Order.Builder()
				.withCreatedOn(LocalDateTime.now())
				.withOrderDetails(order.orderDetails())
				.withCart(order.cart())
				.build();

		OrderValidationResult result = orderValidator.validate(theOrder);

		if (result.isValid()) {
			return pjp.proceed();
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ApiErrorDTO.Builder()
						.withStatusCode(HttpStatus.BAD_REQUEST.value())
						.withPath(request.getServletPath())
						.withErrorMsg(result.getMessage())
						.build());
	}

	@Around(value = "execution(* PizzaApp.api.controllers.locked.UserOrdersController.updateUserOrder(..)) && args(order, request)", argNames = "pjp,order,request")
	public Object validateUserOrderUpdate(ProceedingJoinPoint pjp, UpdateUserOrderDTO order, HttpServletRequest request) throws Throwable {
		Order theOrder = new Order.Builder()
				.withCreatedOn(order.getCreatedOn())
				.withOrderDetails(order.getOrderDetails())
				.withCart(order.getCart())
				.build();

		OrderValidationResult result = orderValidator.validateUpdate(theOrder);

		if (result.isValid() && result.isCartUpdateValid()) {
			return pjp.proceed();
		}

		if (result.isValid() && !result.isCartUpdateValid()) {
			order.setCart(null);
			return pjp.proceed();
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ApiErrorDTO.Builder()
						.withStatusCode(HttpStatus.BAD_REQUEST.value())
						.withPath(request.getServletPath())
						.withErrorMsg(result.getMessage())
						.build());
	}

	@Around(value = "execution(* PizzaApp.api.controllers.locked.UserOrdersController.deleteUserOrderById(..)) && args(orderId," +
			" userId, request)", argNames = "pjp,orderId,userId,request")
	public Object validateUserOrderDelete(ProceedingJoinPoint pjp, Long orderId, Long userId, HttpServletRequest request) throws Throwable {
		OrderValidationResult result = orderValidator.validateDelete(orderId);

		if (result.isValid()) {
			return pjp.proceed();
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ApiErrorDTO.Builder()
						.withStatusCode(HttpStatus.BAD_REQUEST.value())
						.withPath(request.getServletPath())
						.withErrorMsg(result.getMessage())
						.build());
	}
}