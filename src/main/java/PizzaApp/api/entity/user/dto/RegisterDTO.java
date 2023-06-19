package PizzaApp.api.entity.user.dto;

import PizzaApp.api.exceptions.constraints.FieldMatch;
import jakarta.validation.constraints.*;

@FieldMatch.List({
		@FieldMatch(first = "email", second = "matchingEmail", message = "El email debe coincidir"),
		@FieldMatch(first = "password", second = "matchingPassword", message = "La contraseña debe coincidir")})
public class RegisterDTO {

	@Email(message = "Email: compruebe al email introducido")
	@NotBlank(message = "Email: el valor no puede ser vacío")
	private String email;

	@Email(message = "Email: compruebe al email introducido")
	@NotBlank(message = "Email: el valor no puede ser vacío")
	private String matchingEmail;

	@NotNull(message = "La contraseña no puede ser vacía")
	@Size(min = 1, max = 20, message = "La contraseña no puede ser vacía")
	private String password;

	@NotNull(message = "La contraseña no puede ser vacía")
	@Size(min = 1, max = 20, message = "La contraseña no puede ser vacía")
	private String matchingPassword;

	public RegisterDTO() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMatchingEmail() {
		return matchingEmail;
	}

	public void setMatchingEmail(String matchingEmail) {
		this.matchingEmail = matchingEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}
}
