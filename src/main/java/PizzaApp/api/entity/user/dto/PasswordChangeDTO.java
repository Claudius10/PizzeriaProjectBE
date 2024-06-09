package PizzaApp.api.entity.user.dto;

import PizzaApp.api.exceptions.constraints.annotation.FieldMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@FieldMatch.List({
		@FieldMatch(
				first = "newPassword",
				second = "matchingNewPassword",
				message = "La contraseña debe coincidir")})
public record PasswordChangeDTO(
		@NotBlank(message = "La contraseña actual no puede faltar")
		String currentPassword,

		@Size(min = 8, max = 20, message = "La contraseña tiene que contener entre 8-20 caracteres")
		@NotBlank(message = "La nueva contraseña no puede faltar")
		String newPassword,

		@Size(min = 8, max = 20, message = "La contraseña tiene que contener entre 8-20 caracteres")
		@NotBlank(message = "La nueva contraseña no puede faltar")
		String matchingNewPassword) {
}
