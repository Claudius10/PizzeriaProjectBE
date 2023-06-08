package PizzaApp.api.entity.clients.user;

import PizzaApp.api.validation.constraints.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@FieldMatch.List({
		@FieldMatch(first = "password", second = "matchingPassword", message = "La contraseña debe coincidir"),
		@FieldMatch(first = "username", second = "matchingUsername", message = "El email debe coincidir")})
public class UserRegistration {

	@Email(message = "Email: compruebe al email introducido")
	@NotBlank(message = "Email: el valor no puede ser vacío")
	private String username;

	@Email(message = "Email: compruebe al email introducido")
	@NotBlank(message = "Email: el valor no puede ser vacío")
	private String matchingUsername;

	@NotNull(message = "La contraseña no puede ser vacía")
	@Size(min = 1, message = "La contraseña no puede ser vacía")
	private String password;

	@NotNull(message = "La contraseña no puede ser vacía")
	@Size(min = 1, message = "La contraseña no puede ser vacía")
	private String matchingPassword;

	public UserRegistration() {
	}

	public UserRegistration(String username, String matchingUsername, String password, String matchingPassword) {
		this.username = username;
		this.matchingUsername = matchingUsername;
		this.password = password;
		this.matchingPassword = matchingPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMatchingUsername() {
		return matchingUsername;
	}

	public void setMatchingUsername(String matchingUsername) {
		this.matchingUsername = matchingUsername;
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