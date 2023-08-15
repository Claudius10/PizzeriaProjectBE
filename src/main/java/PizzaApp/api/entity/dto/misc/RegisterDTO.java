package PizzaApp.api.entity.dto.misc;

import PizzaApp.api.exceptions.constraints.FieldMatch;
import jakarta.validation.constraints.*;

@FieldMatch.List({
		@FieldMatch(first = "email", second = "matchingEmail", message = "El email debe coincidir"),
		@FieldMatch(first = "password", second = "matchingPassword", message = "La contraseña debe coincidir")})
public class RegisterDTO {

	@Pattern(regexp = "^[a-zA-Z\s]{2,50}$",
			message = "El nombre y apellido(s): solo letras sin tildes (mín 2, máx 25 letras)")
	private String name;

	@Email(message = "Email: compruebe al email introducido")
	@NotBlank(message = "Email: el valor no puede ser vacío")
	private String email;

	@Email(message = "Email: compruebe al email introducido")
	@NotBlank(message = "Email: el valor no puede ser vacío")
	private String matchingEmail;

	@Size(min = 1, max = 20, message = "La contraseña tiene que contener entre 1-20 caracteres")
	private String password;

	@Size(min = 1, max = 20, message = "La contraseña tiene que contener entre 1-20 caracteres")
	private String matchingPassword;

	public RegisterDTO() {
	}

	private RegisterDTO(Builder builder) {
		this.name = builder.name;
		this.email = builder.email;
		this.matchingEmail = builder.matchingEmail;
		this.password = builder.password;
		this.matchingPassword = builder.matchingPassword;
	}

	public static class Builder {

		private String name, email, matchingEmail, password, matchingPassword;

		public Builder() {
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withEmail(String email) {
			this.email = email;
			return this;
		}

		public Builder withMatchingEmail(String matchingEmail) {
			this.matchingEmail = matchingEmail;
			return this;
		}

		public Builder withPassword(String password) {
			this.password = password;
			return this;
		}

		public Builder withMatchingPassword(String matchingPassword) {
			this.matchingPassword = matchingPassword;
			return this;
		}

		public RegisterDTO build() {
			return new RegisterDTO(this);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
