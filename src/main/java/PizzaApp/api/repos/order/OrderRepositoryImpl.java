package PizzaApp.api.repos.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.dto.order.OrderCreatedOnDTO;
import PizzaApp.api.entity.dto.order.OrderDTO;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

	private final EntityManager em;

	public OrderRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public Long createOrUpdate(Order order) {

		// new order
		if (order.getId() == null) {
			// sync bi associations

			order.setOrderDetails(order.getOrderDetails());
			order.setCart(order.getCart());

			List<OrderItem> copy = new ArrayList<>(order.getCart().getOrderItems());
			order.getCart().getOrderItems().clear();

			for (OrderItem item : copy) {
				order.getCart().addItem(item);
			}

			// set user null
			order.setUser(null);
		}

		Order theOrder = em.merge(order);
		return theOrder.getId();
	}

	@Override
	public Order findById(Long id) {
		return em.find(Order.class, id);
	}

	// for fetching order for front-end request
	// Mi Pedido component
	@Override
	public OrderDTO findDTOByIdAndTel(String id, String orderContactTel) {
		try {
			TypedQuery<OrderDTO> query = em.createQuery("""
					select new OrderDTO(
					   o.id,
					   o.createdOn,
					   o.updatedOn,
					   o.customerFirstName,
					   o.customerLastName,
					   o.contactTel,
					   o.address,
					   o.email,
					   o.orderDetails,
					   o.cart
					)
					from Order o
					where o.id= :orderId and o.contactTel= :orderTel
					""", OrderDTO.class);

			query.setParameter("orderId", id);
			query.setParameter("orderTel", orderContactTel);

			return query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	@Override
	public OrderCreatedOnDTO findCreatedOnById(Long id) {
		return em.createQuery("""
				select new OrderCreatedOnDTO(
				   o.id,
				   o.createdOn
				)
				from Order o
				where o.id=: orderId
				""", OrderCreatedOnDTO.class).setParameter("orderId", id).getSingleResult();
	}

	@Override
	public void deleteById(Long id) {
		// find order
		Order order = em.find(Order.class, id);

		// delete
		em.remove(order);
	}
}