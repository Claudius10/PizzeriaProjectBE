package PizzaApp.api.entity.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailChangeDTO(
		@Email(message = "El formato del email introducido no es válido")
		@NotBlank(message = "El email no puede faltar")
		String email,
		@NotBlank(message = "La contraseña no puede faltar")
		String password) {
}
