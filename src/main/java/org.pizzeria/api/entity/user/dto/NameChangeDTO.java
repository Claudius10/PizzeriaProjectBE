package org.pizzeria.api.entity.user.dto;

import jakarta.validation.constraints.Pattern;
import org.pizzeria.api.utils.globals.ValidationResponses;
import org.pizzeria.api.utils.globals.ValidationRules;

public record NameChangeDTO(
		@Pattern(regexp = ValidationRules.USER_NAME, message = ValidationResponses.NAME_INVALID)
		String name,
		String password) {
}