package PizzaApp.api.entity.dto.user;

public record UserOrderData(String userId, Long addressId, Integer tel, String email, String customerName) {
}
