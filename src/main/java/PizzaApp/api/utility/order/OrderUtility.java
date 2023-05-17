package PizzaApp.api.utility.order;
import PizzaApp.api.entity.order.Order;

public interface OrderUtility {

	public boolean isChangeRequestedValid(Order order);

	public boolean isCartEmpty(Order order);

	public Double calculatePaymentChange(Order order);
}
