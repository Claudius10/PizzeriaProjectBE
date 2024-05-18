package PizzaApp.api.entity.user.dto;

import PizzaApp.api.utils.globals.ValidationResponses;
import PizzaApp.api.utils.globals.ValidationRules;
import jakarta.validation.constraints.Pattern;

public record NameChangeDTO(
		@Pattern(regexp = ValidationRules.USER_NAME, message = ValidationResponses.NAME_INVALID)
		String name,
		String password) {
}