package PizzaApp.api.entity.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record LoginDTO(
		@Email String email,
		@Size(min = 1, max = 35) String password) {
}
