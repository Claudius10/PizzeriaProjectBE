package PizzaApp.api.aop.aspects.validation;

import PizzaApp.api.entity.dto.error.ApiErrorDTO;
import PizzaApp.api.entity.order.dto.NewAnonOrderDTO;
import PizzaApp.api.entity.order.dto.UpdateUserOrderDTO;
import PizzaApp.api.entity.order.dto.NewUserOrderDTO;
import PizzaApp.api.validation.order.OrderValidationResult;
import PizzaApp.api.validation.order.OrderValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ValidateOrderOperation {

	private final OrderValidator orderValidator;

	public ValidateOrderOperation(OrderValidator orderValidator) {
		this.orderValidator = orderValidator;
	}

	@Around(value = "execution(* PizzaApp.api.controllers.open.AnonController.createAnonOrder(..)) && args(newAnonOrder, request)", argNames = "pjp,newAnonOrder,request")
	public Object validateNewAnonOrder(ProceedingJoinPoint pjp, NewAnonOrderDTO newAnonOrder, HttpServletRequest request) throws Throwable {
		OrderValidationResult result = orderValidator.validate(newAnonOrder.cart(), newAnonOrder.orderDetails());

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

	@Around(value = "execution(* PizzaApp.api.controllers.locked.user.UserOrdersController.createUserOrder(..)) && args(order, request)", argNames = "pjp,order,request")
	public Object validateNewUserOrder(ProceedingJoinPoint pjp, NewUserOrderDTO order, HttpServletRequest request) throws Throwable {
		OrderValidationResult result = orderValidator.validate(order.cart(), order.orderDetails());

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

	@Around(value = "execution(* PizzaApp.api.controllers.locked.user.UserOrdersController.updateUserOrder(..)) && args(order, request)", argNames = "pjp,order,request")
	public Object validateUserOrderUpdate(ProceedingJoinPoint pjp, UpdateUserOrderDTO order, HttpServletRequest request) throws Throwable {
		OrderValidationResult result = orderValidator.validateUpdate(order.getCart(), order.getOrderDetails(), order.getCreatedOn());

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

	@Around(value = "execution(* PizzaApp.api.controllers.locked.user.UserOrdersController.deleteUserOrderById(..)) && args(orderId,request)", argNames = "pjp,orderId,request")
	public Object validateUserOrderDelete(ProceedingJoinPoint pjp, Long orderId, HttpServletRequest request) throws Throwable {
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