package PizzaApp.api.entity.dto.user;

import jakarta.validation.constraints.Pattern;

public record NameChangeDTO(
		@Pattern(
				regexp = "^[a-zA-Z\s]{2,50}$",
				message = "El nombre y apellido(s): solo letras sin tildes (mín 2, máx 25 letras)")
		String name,
		String password) {
}
