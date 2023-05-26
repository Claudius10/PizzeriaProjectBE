package PizzaApp.api.repos.order;
import java.util.List;
import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.order.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

	private EntityManager em;

	public OrderRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public void createOrUpdate(Order order) {
		// persist the order
		
		// sync bi associations 
		order.setOrderDetails(order.getOrderDetails());
		order.setCart(order.getCart());
		
		// for orders without user
		order.setUser(null);
		
		Order theOrder = em.merge(order);

		// set the newly generated id
		order.setId(theOrder.getId());
	}

	@Override
	public Order findById(Long id) {
		return em.find(Order.class, id);
	}

	@Override
	public void deleteById(Long id) {
		// find order
		Order order = findById(id);
	
		// delete
		em.remove(order);
	}

	// not currently in use

	@Override
	public List<Order> findAll() {
		TypedQuery<Order> query = em.createQuery("from Order", Order.class);
		List<Order> orders = query.getResultList();
		return orders;
	}

	@Override
	public List<Order> findAllByStore(String storeName) {
		TypedQuery<Order> query = em.createQuery("FROM Order o where o.storePickUpName=:storeName", Order.class);
		query.setParameter("storeName", storeName);
		List<Order> orders = query.getResultList();
		return orders;
	}

	@Override
	public List<Order> findAllByCustomer(Long customerId) {
		TypedQuery<Order> query = em.createQuery("from Order o where o.customer.id=:customerId", Order.class);
		query.setParameter("customerId", customerId);
		List<Order> customerOrders = query.getResultList();
		return customerOrders;
	}
}
