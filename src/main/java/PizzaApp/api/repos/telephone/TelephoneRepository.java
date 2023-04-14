package PizzaApp.api.repos.telephone;

import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.clients.customer.Telephone;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class TelephoneRepository {

	private EntityManager em;

	public TelephoneRepository(EntityManager em) {
		this.em = em;
	}

	public Telephone findCustomerTel(int telNumber) {

		try {
			TypedQuery<Telephone> query = em.createQuery("from Telephone t where t.number=:number", Telephone.class);
			query.setParameter("number", telNumber);

			return query.getSingleResult();
		} catch (NoResultException | NullPointerException e) {
			return null;
		}
	}
}
