package PizzaApp.api.repos;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.Order;
import jakarta.persistence.EntityManager;


@Repository
public class OrdersRepository {

	private EntityManager em;

	public OrdersRepository(EntityManager em) {
		this.em = em;
	}

	public void createOrder(Order order) {
		String orderDate = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH:mm:ss"));
		order.getOrderDetails().setOrderDate(orderDate);
		em.persist(order);
	}
}
