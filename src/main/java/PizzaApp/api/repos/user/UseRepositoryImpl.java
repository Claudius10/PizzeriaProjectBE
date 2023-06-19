package PizzaApp.api.repos.user;

import PizzaApp.api.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UseRepositoryImpl implements UserRepository, UserDetailsRepository {

	private final EntityManager em;

	public UseRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public void create(User user) {
		em.persist(user);
	}

	@Override
	public Optional<User> findByUsername(String username) throws RuntimeException {
		TypedQuery<User> query = em.createQuery("select u from User u where u.username = :Username", User.class);
		query.setParameter("Username", username);
		return query.getResultStream().findFirst();
	}
}
