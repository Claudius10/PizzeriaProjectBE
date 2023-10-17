package PizzaApp.api.entity.dto.user;

public record UserOrderDataDTO(
		Long userId,
		Long addressId,
		Integer tel,
		String email,
		String customerName) {
}
