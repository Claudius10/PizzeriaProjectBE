package PizzaApp.api.repos.email;

import org.springframework.stereotype.Repository;

import PizzaApp.api.entity.user.common.Email;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class EmailRepositoryImpl implements EmailRepository {

	private final EntityManager em;

	public EmailRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public Email findByAddress(String address) {
		TypedQuery<Email> query = em.createQuery("select Email from Email e where e.email=:address", Email.class);
		query.setParameter("address", address);
		return query.getSingleResult();
	}
}
