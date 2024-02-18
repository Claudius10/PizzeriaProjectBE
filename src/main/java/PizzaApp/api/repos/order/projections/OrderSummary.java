package PizzaApp.api.repos.order.projections;

public interface OrderSummary {

	Long getId();

	String getFormattedCreatedOn();

	String getFormattedUpdatedOn();

	OrderDetailsView getOrderDetails();

	CartView getCart();

	interface CartView {
		Long getId();

		Integer getTotalQuantity();

		Double getTotalCost();

		Double getTotalCostOffers();
	}

	interface OrderDetailsView {
		String getPaymentType();
	}
}
