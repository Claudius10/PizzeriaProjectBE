package PizzaApp.api.entity.order.dto;

import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.exceptions.constraints.annotation.IntegerLength;
import PizzaApp.api.utils.globals.ValidationResponses;
import PizzaApp.api.utils.globals.ValidationRules;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record NewAnonOrderDTO(

		@Pattern(regexp = ValidationRules.USER_NAME, message = ValidationResponses.NAME_INVALID)
		String anonCustomerName,

		@IntegerLength(min = 9, max = 9, message = ValidationResponses.NUMBER_INVALID)
		Integer anonCustomerContactNumber,

		@Email(message = ValidationResponses.EMAIL_INVALID)
		String anonCustomerEmail,

		@Valid
		Address address,

		@Valid
		OrderDetails orderDetails,

		Cart cart) {
}