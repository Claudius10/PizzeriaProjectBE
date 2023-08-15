package PizzaApp.api.entity.dto.user;

import PizzaApp.api.exceptions.constraints.FieldMatch;
import jakarta.validation.constraints.NotBlank;

@FieldMatch.List({
		@FieldMatch(
				first = "newPassword",
				second = "matchingNewPassword",
				message = "La contraseña debe coincidir")})
public record PasswordChangeDTO(
		@NotBlank(message = "La contraseña actual no puede faltar") String currentPassword,
		@NotBlank(message = "La nueva contraseña no puede faltar") String newPassword,

		@NotBlank(message = "La nueva contraseña no puede faltar") String matchingNewPassword) {
}
