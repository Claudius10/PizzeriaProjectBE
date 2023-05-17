package PizzaApp.api.repos.telephone;

import java.util.List;

import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.clients.customer.Telephone;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class TelephoneRepositoryImpl implements TelephoneRepository {

	private EntityManager em;

	public TelephoneRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public Telephone findByNumber(Telephone telephone) {
		try {
			TypedQuery<Telephone> query = em.createQuery("from Telephone t where t.number=:number", Telephone.class);
			query.setParameter("number", telephone.getNumber());

			return query.getSingleResult();
		} catch (NoResultException | NullPointerException e) {
			return null;
		}
	}

	@Override
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
