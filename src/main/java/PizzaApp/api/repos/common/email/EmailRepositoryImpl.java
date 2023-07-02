package PizzaApp.api.repos.common.email;

import org.springframework.stereotype.Repository;

import PizzaApp.api.entity.common.Email;
import jakarta.persistence.EntityManager;
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
