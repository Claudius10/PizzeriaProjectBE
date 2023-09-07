package PizzaApp.api.repos.user.account;

import PizzaApp.api.entity.user.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class AnonUserRepositoryImpl implements AnonUserRepository {

	private final EntityManager em;

	public AnonUserRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public User create(User user) {
		em.persist(user);
		return user;
	}
}
