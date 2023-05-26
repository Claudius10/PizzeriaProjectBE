package PizzaApp.api.services.order;
import PizzaApp.api.entity.dto.OrderDataDTO;
import PizzaApp.api.entity.order.Order;

public interface OrderDataInternalService {
	public OrderDataDTO findOrderData(Order order);
}
