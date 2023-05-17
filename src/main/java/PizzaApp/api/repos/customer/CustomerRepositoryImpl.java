package PizzaApp.api.repos.customer;
import java.util.List;

import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.clients.customer.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

	private EntityManager em;

	public CustomerRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
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

	// for testing purpose
	@Override
	public List<Customer> findCustomers(Customer customer) {

		TypedQuery<Customer> query = em.createQuery(
				"from Customer c where c.firstName=:cFirstName and c.lastName=:cLastName and c.email=:cEmail",
				Customer.class);
		query.setParameter("cFirstName", customer.getFirstName());
		query.setParameter("cLastName", customer.getLastName());
		query.setParameter("cEmail", customer.getEmail());

		if (query.getResultList().isEmpty()) {
			throw new NoResultException("No customer found matching given customer.");
		} else {
			return query.getResultList();
		}
	}
}