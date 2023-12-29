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
	public String createOrder(Order order) {
		// sync bi associations
		order.setOrderDetails(order.getOrderDetails());
		order.setCart(order.getCart());
		syncCartItems(order);

		em.persist(order);
		return order.getId().toString();
	}

	@Override
	public String updateOrder(Order order) {
		if (order.getCart().getOrderItems() != null) {
			syncCartItems(order);
		}

		Order theOrder = em.merge(order);
		return theOrder.getId().toString();
	}

	@Override
	public OrderDTOPojo findOrderDTO(Long orderId) {
		OrderDTOPojo order = em.createQuery("""
						select new OrderDTOPojo(
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
						""", OrderDTOPojo.class)
				.setParameter("orderId", orderId)
				.getSingleResult();

		// select cart separately as a workaround for the time being
		// since "join order.cart.orderItems" in the above query
		// produces NonUniqueResultException: query did not return a unique result
		order.setCart(findOrderCart(orderId));
		return order;
	}

	@Override
	public Integer findOrderCount(Long userId) {
		return em.createQuery("select count(order) from Order order where order.userData.id = :userId",
				Number.class).setParameter("userId", userId).getSingleResult().intValue();
	}

	@Override
	public List<OrderSummary> findOrderSummaryList(Long userId, int pageSize, int pageNumber) {
		Session session = em.unwrap(Session.class);
		return session.createQuery(
						"select order.id, " +
								"order.createdOn, " +
								"order.updatedOn, " +
								"order.formattedCreatedOn, " +
								"order.formattedUpdatedOn, " +
								"order.orderDetails.paymentType, " +
								"order.cart.totalQuantity, " +
								"order.cart.totalCost, " +
								"order.cart.totalCostOffers " +
								"from Order order where order.userData.id = :userId order by order.id desc", OrderSummary.class)
				.setParameter("userId", userId)
				.setFirstResult((pageNumber - 1) * pageSize)
				.setMaxResults(pageSize)
				.setTupleTransformer((tuple, aliases) -> new OrderSummary(
						(Long) tuple[0],
						(LocalDateTime) tuple[1],
						(LocalDateTime) tuple[2],
						(String) tuple[3],
						(String) tuple[4],
						new OrderDetailsDTO(
								((String) tuple[5])
						),
						new CartDTO(
								((int) tuple[6]),
								((double) tuple[7]),
								((double) tuple[8])
						)
				)).getResultList();
	}

	@Override
	public void deleteById(Long orderId) {
		em.remove(em.getReference(Order.class, orderId));
	}

	// INFO - for internal use only

	@Override
	public OrderCreatedOnDTO findCreatedOnById(Long orderId) {
		return em.createQuery("""
				select new OrderCreatedOnDTO(
				order.createdOn,
				order.formattedCreatedOn
				) from Order order where order.id= :orderId
				""", OrderCreatedOnDTO.class).setParameter("orderId", orderId).getSingleResult();
	}

	@Override
	public Order findById(Long orderId) {
		return em.createQuery("select order from Order order " +
						"join fetch order.cart as cart join fetch cart.orderItems " +
						"where order.id = :orderId", Order.class)
				.setParameter("orderId", orderId)
				.getSingleResult();
	}

	// used when user deletes account
	@Override
	public void removeUserData(Long userId) {
		em.createQuery("update Order order set order.contactTel = null, " +
						"order.email = null, " +
						"order.customerName = null " +
						"where order.userData.id = :userId")
				.setParameter("userId", userId)
				.executeUpdate();
	}

	@Override
	public Order findReferenceById(Long orderId) {
		return em.getReference(Order.class, orderId);
	}

	@Override
	public Cart findOrderCart(Long orderId) {
		return em.createQuery("select order.cart from Order order join fetch order.cart.orderItems where order.id = :orderId", Cart.class).
				setParameter("orderId", orderId)
				.getSingleResult();
	}

	// INFO - util method

	public void syncCartItems(Order order) {
		List<OrderItem> copy = new ArrayList<>(order.getCart().getOrderItems());
		order.getCart().getOrderItems().clear();

		for (OrderItem item : copy) {
			order.getCart().addItem(item);
		}
	}
}