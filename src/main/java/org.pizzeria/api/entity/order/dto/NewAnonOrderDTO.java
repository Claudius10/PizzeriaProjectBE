package org.pizzeria.api.entity.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.entity.order.Cart;
import org.pizzeria.api.entity.order.OrderDetails;
import org.pizzeria.api.exceptions.constraints.annotation.IntegerLength;
import org.pizzeria.api.utils.globals.ValidationResponses;
import org.pizzeria.api.utils.globals.ValidationRules;

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