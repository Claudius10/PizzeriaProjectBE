package org.pizzeria.api.web.dto.user.dto;

import jakarta.validation.constraints.Pattern;
import org.pizzeria.api.web.globals.ValidationResponses;
import org.pizzeria.api.web.globals.ValidationRules;

public record NameChangeDTO(
		@Pattern(regexp = ValidationRules.SIMPLE_LETTERS_ONLY_MAX_50_INSENSITIVE_REQUIERED, message = ValidationResponses.NAME_INVALID)
		String name,
		String password) {
}