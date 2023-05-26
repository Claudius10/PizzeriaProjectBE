package PizzaApp.api.repos.user;
import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.clients.Email;
import PizzaApp.api.entity.clients.Telephone;
import PizzaApp.api.entity.clients.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class UserRepositoryImpl implements UserRepository {

	private EntityManager em;

	public UserRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public void create(Email email) {
		em.persist(email);
	}

	@Override
	public User findByTel(Telephone tel) {
		try {
			TypedQuery<User> query = em.createQuery("from User u where u.tel=: telNumber", User.class);
			query.setParameter("telNumber", tel);
			return query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}

	}

	@Override
	public User findByEmail(Email address) {
		try {
			TypedQuery<User> query = em.createQuery("from User u where u.email=: address", User.class);
			query.setParameter("address", address);
			return query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	@Override
	public void deleteByTel(Telephone tel) {
		em.remove(findByTel(tel));
	}
}
