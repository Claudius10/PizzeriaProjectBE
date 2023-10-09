package PizzaApp.api.entity.dto.misc;

import PizzaApp.api.exceptions.constraints.FieldMatch;
import jakarta.validation.constraints.*;

@FieldMatch.List({
		@FieldMatch(first = "email", second = "matchingEmail", message = "El email debe coincidir"),
		@FieldMatch(first = "password", second = "matchingPassword", message = "La contraseña debe coincidir")})
public record RegisterDTO(
		@Pattern(
				regexp = "^[a-zA-Z\sÁÉÍÓÚáéíóúÑñ]{2,50}$",
				message = "Compruebe que el nombre y apellido(s) esté compuesto solo por un mínimo de 2 y un máximo de " +
						"50 letras")
		String name,

		@Email(message = "Email: compruebe al email introducido")
		@NotBlank(message = "Email: el valor no puede ser vacío")
		String email,

		@Email(message = "Email: compruebe al email introducido")
		@NotBlank(message = "Email: el valor no puede ser vacío")
		String matchingEmail,

		@Size(min = 8, max = 20, message = "La contraseña tiene que contener entre 8-20 caracteres")
		String password,

		@Size(min = 8, max = 20, message = "La contraseña tiene que contener entre 8-20 caracteres")
		String matchingPassword) {
}
