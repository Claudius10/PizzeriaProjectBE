package PizzaApp.api.repos.user.account;

import PizzaApp.api.entity.dto.user.PasswordDTO;
import PizzaApp.api.entity.dto.user.UserDataDTO;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.entity.user.UserData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

	private final EntityManager em;

	public AccountRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public User findUserReference(String id) {
		return em.getReference(User.class, id);
	}

	@Override
	public UserData findReference(String id) {
		return em.getReference(UserData.class, id);
	}

	@Override
	public Optional<UserData> findDataById(Long id) {
		UserData userData = em.find(UserData.class, id);
		return Optional.of(userData);
	}

	@Override
	public Optional<UserDataDTO> findDataDTOById(Long id) {
		TypedQuery<UserDataDTO> query = em.createQuery("""
				select new UserDataDTO(
				ud.id,
				ud.name,
				ud.email
				)
				from User ud
				where ud.id= :userDataId
				""", UserDataDTO.class);
		query.setParameter("userDataId", id);
		return query.getResultStream().findFirst();
	}

	@Override
	public void createData(UserData userData) {
		em.persist(userData);
	}

	@Override
	public void updateEmail(Long id, String email) {

		Query userQuery = em.createQuery("update User user set user.email =: email " +
				"where user.id =: id");

		userQuery.setParameter("id", id);
		userQuery.setParameter("email", email);
		userQuery.executeUpdate();

		Query userDataQuery = em.createQuery("update UserData user set user.email =: email " +
				"where user.id =: id");

		userDataQuery.setParameter("id", id);
		userDataQuery.setParameter("email", email);
		userDataQuery.executeUpdate();
	}

	@Override
	public void updatePassword(Long id, String password) {
		Query query = em.createQuery("update User user set user.password = :password where user.id = :id");
		query.setParameter("id", id);
		query.setParameter("password", password);
		query.executeUpdate();
	}

	@Override
	public void updateName(String id, String name) {
		Query query = em.createQuery("update UserData user set user.name = :name where user.id = :id");
		query.setParameter("name", name);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public void delete(String id) {
		em.remove(findReference(id));
		em.remove(findUserReference(id));
	}

	@Override
	public String loadPassword(String id) {
		return em.createQuery("""
							select new PasswordDTO(
							u.password
							)
							from User u
							where u.id = :id
				""", PasswordDTO.class).setParameter("id", id).getSingleResult().password();
	}
}
