package PizzaApp.api.entity.user.dto;

import PizzaApp.api.exceptions.constraints.IntegerLength;
import jakarta.validation.constraints.NotBlank;

public record ContactNumberChangeDTO(
		@IntegerLength(min = 9, max = 9, message = "Teléfono: mín 9 dígitos, máx 9 dígitos")
		Integer contactNumber,

		@NotBlank(message = "La contraseña no puede faltar")
		String password) {
}





