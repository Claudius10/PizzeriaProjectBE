package PizzaApp.api.entity.dto.misc;

import jakarta.validation.constraints.Size;

public class LoginDTO {

	@Size(min = 1, max = 35)
	private String email;

	@Size(min = 1, max = 35)
	private String password;

	public LoginDTO(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
