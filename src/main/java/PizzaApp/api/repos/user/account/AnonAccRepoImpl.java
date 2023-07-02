package PizzaApp.api.repos.user.account;

import PizzaApp.api.entity.user.User;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AnonAccRepoImpl implements AnonAccRepo {

	private final EntityManager em;

	public AnonAccRepoImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public User create(User user) {
		em.persist(user);
		return user;
	}
}
