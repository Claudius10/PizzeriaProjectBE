package org.pizzeria.api.entity.order.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.pizzeria.api.exceptions.constraints.annotation.IntegerLength;
import org.pizzeria.api.utils.globals.ValidationResponses;
import org.pizzeria.api.utils.globals.ValidationRules;

public record CustomerDTO(
		@Pattern(regexp = ValidationRules.USER_NAME, message = ValidationResponses.NAME_INVALID)
		String name,

		@IntegerLength(min = 9, max = 9, message = ValidationResponses.NUMBER_INVALID)
		Integer contactNumber,

		@Email(message = ValidationResponses.EMAIL_INVALID)
		String email
) {
}
