package PizzaApp.api.repos.customer;

import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.clients.customer.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class CustomerRepository {

	private EntityManager em;

	public CustomerRepository(EntityManager em) {
		this.em = em;
	}

	public Customer findCustomer(Customer customer) {

		try {
			TypedQuery<Customer> query = em.createQuery(
					"from Customer c where c.firstName=:cFirstName and c.lastName=:cLastName and c.email=:cEmail",
					Customer.class);

			query.setParameter("cFirstName", customer.getFirstName());
			query.setParameter("cLastName", customer.getLastName());
			query.setParameter("cEmail", customer.getEmail());
			return query.getSingleResult();
		} catch (NoResultException | NullPointerException e) {
			return null;
		}
	}
}
