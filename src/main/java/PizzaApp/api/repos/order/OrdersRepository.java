package PizzaApp.api.repos.order;
import java.util.List;
import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.order.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class OrdersRepository {

	private EntityManager em;

	public OrdersRepository(EntityManager em) {
		this.em = em;
	}

	// create / update
	public void createOrUpdate(Order order) {

		// persist the order
		Order theOrder = em.merge(order);

		// set the newly generated id in the DB to return it to front-end
		order.setId(theOrder.getId());
	}

	// read

	public Order findById(Long id) {
		return em.find(Order.class, id);
	}

	// delete
	public void deleteById(Long id) {
		em.remove(findById(id));
	}

	// currently not in use

	public List<Order> findAll() {
		TypedQuery<Order> query = em.createQuery("from Order", Order.class);
		List<Order> orders = query.getResultList();
		return orders;
	}

	public List<Order> findAllByStore(String storeName) {
		TypedQuery<Order> query = em.createQuery("FROM Order o where o.storePickUpName=:storeName", Order.class);
		query.setParameter("storeName", storeName);
		List<Order> orders = query.getResultList();
		return orders;
	}

	public List<Order> findAllByCustomer(Long customerId) {
		TypedQuery<Order> query = em.createQuery("from Order o where o.customer.id=:customerId", Order.class);
		query.setParameter("customerId", customerId);
		List<Order> customerOrders = query.getResultList();
		return customerOrders;
	}
}
