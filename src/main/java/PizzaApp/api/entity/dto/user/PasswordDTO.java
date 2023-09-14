package PizzaApp.api.entity.dto.user;

import jakarta.validation.constraints.NotBlank;

public record PasswordDTO(@NotBlank(message = "La contraseña no puede faltar") String password) {
}
