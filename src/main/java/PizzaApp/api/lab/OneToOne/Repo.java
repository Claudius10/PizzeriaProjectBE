package PizzaApp.api.lab.OneToOne;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class Repo {

	private EntityManager em;

	public Repo(EntityManager em) {
		this.em = em;
	}

	// read order
	public OrderLab findOrderById(Long orderId) {
		return em.find(OrderLab.class, orderId);
	}
	
	// read address uni
	public AddressLab findAddressById(Long addressId) {
		AddressLab dbAddress = em.find(AddressLab.class, addressId);
		return em.find(AddressLab.class, addressId);
	}
	
	// read address bi
	public AddressLabBi findBiAddressById(Long addressId) {
		return em.find(AddressLabBi.class, addressId);
	}

	// read all orders
	public List<OrderLab> findAllOrders() {
		TypedQuery<OrderLab> query = em.createQuery("FROM OrderLab", OrderLab.class);
		List<OrderLab> orders = query.getResultList();
		return orders;
	}

	// create order / update order
	public void createOrUpdateOrder(OrderLab order) {
		// create
		if (order.getId() == 0) {
			// insert because id is 0
			OrderLab newOrder = em.merge(order);
			// set the generated id for order and address in the response body
			order.setId(newOrder.getId());
			order.getAddressLab().setId(newOrder.getAddressLab().getId());
		} else {
			// update
			// get existing order
			OrderLab dbOrder = findOrderById(order.getId());
			// set the address id to update address, not insert a new one
			order.getAddressLab().setId(dbOrder.getAddressLab().getId());
			// update order
			OrderLab updatedOrder = em.merge(order);
			// set ids for the response body
			order.setId(updatedOrder.getId());
			order.getAddressLab().setId(updatedOrder.getAddressLab().getId());
		}
	}

	// delete order
	public void deleteOrderById(Long orderId) {
		OrderLab orderToDelete = findOrderById(orderId);
		em.remove(orderToDelete);
	}
}
