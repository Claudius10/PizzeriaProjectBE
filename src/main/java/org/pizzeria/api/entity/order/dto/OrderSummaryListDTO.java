package org.pizzeria.api.entity.order.dto;

import org.pizzeria.api.entity.order.projections.OrderSummaryProjection;

import java.util.List;

public record OrderSummaryListDTO(

		List<OrderSummaryProjection> orderList,
		int totalPages,
		int pageSize
) {
}