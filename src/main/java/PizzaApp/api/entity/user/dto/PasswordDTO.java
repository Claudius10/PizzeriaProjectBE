package PizzaApp.api.entity.user.dto;

import PizzaApp.api.utils.globals.ValidationResponses;
import jakarta.validation.constraints.NotBlank;

public record PasswordDTO(
		@NotBlank(message = ValidationResponses.PASSWORD_MISSING)
		String password) {
}