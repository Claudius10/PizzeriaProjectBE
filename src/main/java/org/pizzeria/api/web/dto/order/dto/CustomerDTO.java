package org.pizzeria.api.web.dto.order.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.pizzeria.api.web.exceptions.constraints.annotation.IntegerLength;
import org.pizzeria.api.web.constants.ValidationResponses;
import org.pizzeria.api.web.constants.ValidationRules;

public record CustomerDTO(
		@Pattern(regexp = ValidationRules.SIMPLE_LETTERS_ONLY_MAX_50_INSENSITIVE_REQUIERED, message = ValidationResponses.NAME_INVALID)
		String name,

		@IntegerLength(min = 9, max = 9, message = ValidationResponses.NUMBER_INVALID)
		Integer contactNumber,

		@Email(message = ValidationResponses.EMAIL_INVALID)
		String email
) {
}
