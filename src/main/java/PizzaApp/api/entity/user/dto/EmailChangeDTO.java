package PizzaApp.api.entity.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailChangeDTO(
		@Email(message = "Formato inválido. Ejemplo formato válido: correo15@gmail.com")
		@NotBlank(message = "El email no puede faltar")
		String email,

		@NotBlank(message = "La contraseña no puede faltar")
		String password) {
}
