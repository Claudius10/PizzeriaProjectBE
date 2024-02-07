package PizzaApp.api.entity.dto.auth;

public record AuthDTO(Long userId, String name, String email, String accessToken, String refreshToken) {
}
