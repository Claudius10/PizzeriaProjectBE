package org.pizzeria.api.entity.order.dto;

import org.pizzeria.api.repos.order.projections.OrderSummary;

import java.util.List;

public record OrderSummaryListDTO(

		List<OrderSummary> orderList,
		int totalPages
) {
}