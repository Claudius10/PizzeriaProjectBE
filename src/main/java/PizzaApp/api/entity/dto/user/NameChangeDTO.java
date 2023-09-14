package PizzaApp.api.entity.dto.user;

import jakarta.validation.constraints.Pattern;

public record NameChangeDTO(
		@Pattern(
				regexp = "^[a-zA-Z\sÁÉÍÓÚáéíóúÑñ]{2,50}$",
				message = "Compruebe que el formato esté compuesto por un mínimo de 2 y un máximo de 50 letras)")
		String name,
		String password) {
}
