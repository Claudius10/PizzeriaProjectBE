package PizzaApp.api.repos.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import PizzaApp.api.entity.dto.order.*;
import PizzaApp.api.entity.order.cart.Cart;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.OrderItem;
import jakarta.persistence.EntityManager;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

	private final EntityManager em;

	public OrderRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public Long createOrder(Order order) {
		// sync bi associations
		order.setOrderDetails(order.getOrderDetails());
		order.setCart(order.getCart());
		syncCartItems(order);

		em.persist(order);
		return order.getId();
	}

	@Override
	public Long updateOrder(Order order) {
		if (order.getCart().getOrderItems() != null) {
			syncCartItems(order);
		}

		Order theOrder = em.merge(order);
		return theOrder.getId();
	}

	@Override
	public OrderDTO findOrder(String id) {
		return em.createQuery("""
				select new OrderDTO(
				   order.id,
				   order.createdOn,
				   order.updatedOn,
				   order.formattedCreatedOn,
				   order.formattedUpdatedOn,
				   order.customerName,
				   order.contactTel,
				   order.email,
				   order.address,
				   order.orderDetails,
				   order.cart
				)
				from Order order
				where order.id = :orderId
				""", OrderDTO.class).setParameter("orderId", id).getSingleResult();
	}

	@Override
	public Number findOrderCount(String userId) {
		return em.createQuery("select count(order) from Order order where order.userData.id = :userId",
				Number.class).setParameter("userId", userId).getSingleResult();
	}

	@Override
	public List<OrderSummary> findOrderSummaryList(String userId, int pageSize, int pageNumber) {
		Session session = em.unwrap(Session.class);
		return session.createQuery(
						"select order.id, " +
								"order.createdOn, " +
								"order.updatedOn, " +
								"order.formattedCreatedOn, " +
								"order.formattedUpdatedOn, " +
								"order.cart.totalQuantity, " +
								"order.cart.totalCost, " +
								"order.cart.totalCostOffers " +
								"from Order order where order.userData.id = :userId order by order.id", OrderSummary.class)
				.setParameter("userId", userId)
				.setFirstResult((pageNumber - 1) * pageSize)
				.setMaxResults(pageSize)
				.setTupleTransformer((tuple, aliases) -> new OrderSummary(
						(Long) tuple[0],
						(LocalDateTime) tuple[1],
						(LocalDateTime) tuple[2],
						(String) tuple[3],
						(String) tuple[4],
						new CartDTO(
								((int) tuple[5]),
								((double) tuple[6]),
								((double) tuple[7])
						)
				)).getResultList();
	}

	@Override
	public void deleteById(String id) {
		em.remove(em.getReference(Order.class, id));
	}

	// NOTE - for internal use only

	@Override
	public OrderCreatedOnDTO findCreatedOnById(String id) {
		return em.createQuery("""
				select new OrderCreatedOnDTO(
				order.createdOn,
				order.formattedCreatedOn
				) from Order order where order.id= :orderId
				""", OrderCreatedOnDTO.class).setParameter("orderId", id).getSingleResult();
	}

	@Override
	public Order findById(String id) {
		return em.find(Order.class, id);
	}

	@Override
	public Order findReferenceById(String id) {
		return em.getReference(Order.class, id);
	}

	@Override
	public Cart findOrderCart(String id) {
		return em.createQuery("select order.cart from Order order where order.id = :orderId", Cart.class).
				setParameter("orderId", id)
				.getSingleResult();
	}

	// NOTE - util method

	public void syncCartItems(Order order) {
		List<OrderItem> copy = new ArrayList<>(order.getCart().getOrderItems());
		order.getCart().getOrderItems().clear();

		for (OrderItem item : copy) {
			order.getCart().addItem(item);
		}
	}
}