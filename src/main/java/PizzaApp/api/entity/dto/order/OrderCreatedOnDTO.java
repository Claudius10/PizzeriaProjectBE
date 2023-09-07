package PizzaApp.api.entity.dto.order;

import java.time.LocalDateTime;

public record OrderCreatedOnDTO(
		LocalDateTime createdOn,
		String formattedCreatedOn
) {
}
