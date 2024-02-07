package PizzaApp.api.entity.dto.auth;

import PizzaApp.api.exceptions.constraints.FieldMatch;
import jakarta.validation.constraints.*;

@FieldMatch.List({
		@FieldMatch(first = "email", second = "matchingEmail", message = "El email debe coincidir"),
		@FieldMatch(first = "password", second = "matchingPassword", message = "La contraseña debe coincidir")})
public record RegisterDTO(
		@Pattern(
				regexp = "^[a-zA-Z\sÁÉÍÓÚáéíóúÑñ]{2,50}$",
				message = "Formato inválido. Ejemplo formato válido: José Miguel")
		String name,

		@Email(message = "Formato inválido. Ejemplo formato válido: correos15@gmail.com")
		@NotBlank(message = "El email no puede faltar")
		String email,

		@Email(message = "Formato inválido. Ejemplo formato válido: correos15@gmail.com")
		@NotBlank(message = "El email no puede faltar")
		String matchingEmail,

		@Size(min = 8, max = 20, message = "La contraseña tiene que contener entre 8-20 caracteres")
		String password,

		@Size(min = 8, max = 20, message = "La contraseña tiene que contener entre 8-20 caracteres")
		String matchingPassword) {
}
