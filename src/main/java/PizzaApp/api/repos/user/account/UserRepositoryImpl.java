package PizzaApp.api.repos.user.account;

import PizzaApp.api.entity.dto.user.PasswordDTO;
import PizzaApp.api.entity.dto.user.UserDTO;
import PizzaApp.api.entity.user.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

	private final EntityManager em;

	public UserRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public User create(User user) {
		em.persist(user);
		return user;
	}

	@Override
	public UserDTO findDTOById(Long userId) {
		return em.createQuery("""
						select new UserDTO(
						user.id,
						user.name,
						user.email
						)
						from User user
						where user.id = :userId
						""", UserDTO.class)
				.setParameter("userId", userId)
				.getSingleResult();
	}

	@Override
	public User findReference(Long userId) {
		return em.getReference(User.class, userId);
	}

	@Override
	public void updateName(Long userId, String name) {
		em.createQuery("update User user set user.name = :name where user.id = :id")
				.setParameter("name", name)
				.setParameter("id", userId)
				.executeUpdate();
	}

	@Override
	public void updateEmail(Long userId, String email) {
		em.createQuery("update User user set user.email = :email where user.id = :id")
				.setParameter("id", userId)
				.setParameter("email", email)
				.executeUpdate();
	}

	@Override
	public void updatePassword(Long userId, String password) {
		em.createQuery("update User user set user.password = :password where user.id = :id")
				.setParameter("id", userId)
				.setParameter("password", password)
				.executeUpdate();
	}

	// for internal use only
	@Override
	public String loadPassword(Long userId) {
		return em.createQuery("""
						select new PasswordDTO(
						u.password
						)
						from User u where u.id = :id""",
				PasswordDTO.class).setParameter("id", userId).getSingleResult().password();
	}
}
