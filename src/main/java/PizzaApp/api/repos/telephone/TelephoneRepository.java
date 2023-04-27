package PizzaApp.api.repos.telephone;
import java.util.List;

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

	public Telephone findByNumber(Telephone telephone) {

		try {
			TypedQuery<Telephone> query = em.createQuery("from Telephone t where t.number=:number", Telephone.class);
			query.setParameter("number", telephone.getNumber());

			return query.getSingleResult();
		} catch (NoResultException | NullPointerException e) {
			return null;
		}
	}

	// method used for testing purposes
	public List<Telephone> findAllByNumber(Telephone telephone) {
		TypedQuery<Telephone> query = em.createQuery("from Telephone t where t.number=:number", Telephone.class);
		query.setParameter("number", telephone.getNumber());

		if (query.getResultList().isEmpty()) {
			throw new NoResultException("No telephone found matching given telephone.");
		} else {
			return query.getResultList();
		}
	}
}
