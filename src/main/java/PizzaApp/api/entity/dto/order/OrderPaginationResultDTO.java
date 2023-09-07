package PizzaApp.api.entity.dto.order;

import java.util.List;

public record OrderPaginationResultDTO(
		int currentPage,
		int totalPages,
		int pageSize,
		int totalOrders,
		List<OrderSummary> orders) {
}
