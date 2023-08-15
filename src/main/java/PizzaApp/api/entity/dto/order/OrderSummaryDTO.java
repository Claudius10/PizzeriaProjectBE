package PizzaApp.api.entity.dto.order;

import java.time.LocalDateTime;

public record OrderSummaryDTO(
		Long id,
		LocalDateTime createdOn,
		LocalDateTime updatedOn,
		String formattedCreatedOn,
		String formattedUpdatedOn,
		CartDTO cart) {
}
