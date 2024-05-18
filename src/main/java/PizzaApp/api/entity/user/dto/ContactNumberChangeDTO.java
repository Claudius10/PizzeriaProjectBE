package PizzaApp.api.entity.user.dto;

import PizzaApp.api.exceptions.constraints.IntegerLength;
import PizzaApp.api.utils.globals.ValidationResponses;
import jakarta.validation.constraints.NotBlank;

public record ContactNumberChangeDTO(
		@IntegerLength(min = 9, max = 9, message = ValidationResponses.NUMBER_INVALID)
		Integer contactNumber,

		@NotBlank(message = ValidationResponses.PASSWORD_MISSING)
		String password) {
}