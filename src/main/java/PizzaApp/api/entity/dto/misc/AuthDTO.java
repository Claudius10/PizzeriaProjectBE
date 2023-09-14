package PizzaApp.api.entity.dto.misc;

public record AuthDTO(Long userId, String name, String email, String accessToken, String refreshToken) {
}
