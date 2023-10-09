package PizzaApp.api.repos.user.account;

import PizzaApp.api.entity.dto.user.PasswordDTO;
import PizzaApp.api.entity.user.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

	private final EntityManager em;
	private final UserDataRepository userDataRepository;

	public UserRepositoryImpl
			(EntityManager em,
			 UserDataRepository userDataRepository) {
		this.em = em;
		this.userDataRepository = userDataRepository;
	}

	@Override
	public User create(User user) {
		em.persist(user);
		return user;
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return em.createQuery("select user from User user where user.email = :email", User.class)
				.setParameter("email", email)
				.getResultStream().findFirst();
	}

	@Override
	public User findReference(String id) {
		return em.getReference(User.class, id);
	}

	@Override
	public void updateName(String id, String name) {
		em.createQuery("update User user set user.name = :name where user.id = :id")
				.setParameter("name", name)
				.setParameter("id", id)
				.executeUpdate();
	}

	@Override
	public void updateEmail(String id, String email) {
		em.createQuery("update User user set user.email = :email where user.id = :id")
				.setParameter("id", id)
				.setParameter("email", email)
				.executeUpdate();
	}

	@Override
	public void updatePassword(String id, String password) {
		em.createQuery("update User user set user.password = :password where user.id = :id")
				.setParameter("id", id)
				.setParameter("password", password)
				.executeUpdate();
	}

	@Override
	public String loadPassword(String id) {
		return em.createQuery("""
						select new PasswordDTO(
						u.password
						)
						from User u where u.id = :id""",
				PasswordDTO.class).setParameter("id", id).getSingleResult().password();
	}

	@Override
	public void delete(String id) {
		em.remove(findReference(id));
		em.remove(userDataRepository.findReference(id));
	}
}
