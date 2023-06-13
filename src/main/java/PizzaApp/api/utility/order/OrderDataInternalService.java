package PizzaApp.api.utility.order;

import PizzaApp.api.entity.order.Order;

public interface OrderDataInternalService {
	OrderData findOrderData(Order order);
}
