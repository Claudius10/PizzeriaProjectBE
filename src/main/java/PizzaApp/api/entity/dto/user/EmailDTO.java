package PizzaApp.api.entity.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailDTO {

	@Email(message = "El formato del email introducido no es válido")
	@NotBlank(message = "El email no puede faltar")
	private String email;

	public EmailDTO() {
	}

	public String getEmail() {
		return email;
	}
}
