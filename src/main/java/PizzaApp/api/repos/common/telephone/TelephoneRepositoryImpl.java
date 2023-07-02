package PizzaApp.api.repos.common.telephone;

import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.common.Telephone;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class TelephoneRepositoryImpl implements TelephoneRepository {

	private final EntityManager em;

	public TelephoneRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public Telephone findByNumber(int telNumber) {
		try {
			TypedQuery<Telephone> query = em.createQuery("from Telephone t where t.number=:number", Telephone.class);
			query.setParameter("number", telNumber);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
