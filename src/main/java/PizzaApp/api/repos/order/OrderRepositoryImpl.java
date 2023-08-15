package PizzaApp.api.repos.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import PizzaApp.api.entity.dto.order.*;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

	private final EntityManager em;

	public OrderRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public Long createOrder(Order order) {
		// sync bi associations
		// NOTE doesn't jackson already use these when data binding?
		order.setOrderDetails(order.getOrderDetails());
		order.setCart(order.getCart());

		// TODO - make this into an util method
		List<OrderItem> copy = new ArrayList<>(order.getCart().getOrderItems());
		order.getCart().getOrderItems().clear();

		for (OrderItem item : copy) {
			order.getCart().addItem(item);
		}

		em.persist(order);
		return order.getId();
	}

	@Override
	public Long updateUserOrder(Order order) {
		Order theOrder = em.merge(order);
		return theOrder.getId();
	}


	@Override
	public Order findById(Long id) {
		return em.find(Order.class, id);
	}

	@Override
	public Order findReferenceById(Long id) {
		return em.getReference(Order.class, id);
	}

	@Override
	public OrderPaginationResultDTO findOrdersSummary(Long userId, int pageSize, int pageNumber) {

		// TODO make this into its own method
		// 1. Find the count of Order rows associated to a user id
		TypedQuery<Number> countQuery = em.createQuery("select count(o) from Order o where o.userData.id=:userId", Number.class);
		countQuery.setParameter("userId", userId);
		int totalOrders = countQuery.getSingleResult().intValue();

		int totalPages;
		if (totalOrders % pageSize == 0) {
			totalPages = totalOrders / pageSize;
		} else {
			totalPages = (totalOrders / pageSize) + 1;
		}

		// TODO make this into its own method
		Session session = em.unwrap(Session.class);
		List<OrderSummaryDTO> resultList = session.createQuery(
						"select o.id, o.createdOn, o.updatedOn, o.cart.totalQuantity, o.cart.totalCost, o.cart.totalCostOffers " +
								"from Order o where o.userData.id=:userId order by o.id", OrderSummaryDTO.class)
				.setParameter("userId", userId)
				.setFirstResult((pageNumber - 1) * pageSize)
				.setMaxResults(pageSize)
				.setTupleTransformer((tuple, aliases) -> new OrderSummaryDTO(
						(Long) tuple[0],
						(LocalDateTime) tuple[1],
						(LocalDateTime) tuple[2],
						"",
						"",
						new CartDTO(
								((int) tuple[3]),
								((double) tuple[4]),
								((double) tuple[5])
						)
				)).getResultList();
		return new OrderPaginationResultDTO(
				pageNumber,
				totalPages,
				pageSize,
				totalOrders,
				resultList);
	}

	@Override
	public OrderDTO findUserOrder(String id) {
		// TODO - check SQL output for this query to check if
		// there is an additional query for cart
		// if there is try to use join fetch
		TypedQuery<OrderDTO> query = em.createQuery("""
				select new OrderDTO(
				   o.id,
				   o.createdOn,
				   o.updatedOn,
				   o.customerName,
				   o.contactTel,
				   o.email,
				   o.address,
				   o.orderDetails,
				   o.cart
				)
				from Order o
				where o.id= :orderId
				""", OrderDTO.class);
		query.setParameter("orderId", id);
		return query.getSingleResult();
	}

	@Override
	public LocalDateTime findCreatedOnById(Long id) {
		return em.createQuery(" select o.createdOn from Order o where o.id=: orderId", LocalDateTime.class)
				.setParameter("orderId", id)
				.getSingleResult();
	}

	@Override
	public void deleteById(Long id) {
		// find order
		Order order = em.getReference(Order.class, id);

		// delete
		em.remove(order);
	}
}