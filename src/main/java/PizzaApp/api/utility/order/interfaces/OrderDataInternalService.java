package PizzaApp.api.utility.order.interfaces;

import PizzaApp.api.entity.order.Order;
import PizzaApp.api.utility.order.OrderData;

public interface OrderDataInternalService {
	OrderData findOrderData(Order order);
}
