package PizzaApp.api.entity.dto.order;

public record CartDTO(
		int totalQuantity,
		double totalCost,
		double totalCostOffers) {
}
