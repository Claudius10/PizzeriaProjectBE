package PizzaApp.api.repos.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import PizzaApp.api.entity.dto.order.*;
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
	public Long updateUserOrder(Order order) {
		syncCartItems(order);
		Order theOrder = em.merge(order);
		return theOrder.getId();
	}

	@Override
	public OrderDTO findUserOrder(String id) {
		return em.createQuery("""
				select new OrderDTO(
				   o.id,
				   o.createdOn,
				   o.updatedOn,
				   o.formattedCreatedOn,
				   o.formattedUpdatedOn,
				   o.customerName,
				   o.contactTel,
				   o.email,
				   o.address,
				   o.orderDetails,
				   o.cart
				)
				from Order o
				where o.id= :orderId
				""", OrderDTO.class).setParameter("orderId", id).getSingleResult();
	}

	@Override
	public Number findUserOrderCount(String userId) {
		return em.createQuery("select count(o) from Order o where o.userData.id= :userId",
				Number.class).setParameter("userId", userId).getSingleResult();
	}

	@Override
	public Optional<List<OrderSummary>> findOrderSummaryList(String userId, int pageSize, int pageNumber) {
		Session session = em.unwrap(Session.class);

		List<OrderSummary> orderSummaryList = session.createQuery(
						"select o.id, o.createdOn, o.updatedOn, o.formattedCreatedOn, o.formattedUpdatedOn, " +
								"o.cart.totalQuantity, o.cart.totalCost, o.cart.totalCostOffers " +
								"from Order o where o.userData.id= :userId order by o.id", OrderSummary.class)
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

		return Optional.of(orderSummaryList);
	}

	@Override
	public void deleteById(String id) {
		// find order
		Order order = em.getReference(Order.class, id);

		// delete
		em.remove(order);
	}

	// NOTE - for internal use only

	@Override
	public OrderCreatedOnDTO findCreatedOnById(String id) {
		return em.createQuery("""
				select new OrderCreatedOnDTO(
				o.createdOn,
				o.formattedCreatedOn
				) from Order o where o.id= :orderId
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

	// NOTE - util method

	public void syncCartItems(Order order) {
		List<OrderItem> copy = new ArrayList<>(order.getCart().getOrderItems());
		order.getCart().getOrderItems().clear();

		for (OrderItem item : copy) {
			order.getCart().addItem(item);
		}
	}
}