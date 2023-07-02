package PizzaApp.api.repos.user.account;

import PizzaApp.api.entity.dto.user.UserDataDTO;
import PizzaApp.api.entity.user.UserData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

	private final EntityManager em;

	public AccountRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public Optional<UserDataDTO> findDataById(Long id) {
		TypedQuery<UserDataDTO> query = em.createQuery("""
				select new UserDataDTO(
				ud.id,
				ud.name,
				ud.email
				)
				from UserData ud
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
		Query query = em.createQuery("update User user set user.email =: email " +
				"where user.id =: id");
		query.setParameter("id", id);
		query.setParameter("email", email);
		query.executeUpdate();
	}

	@Override
	public void updatePassword(Long id, String password) {
		Query query = em.createQuery("update User user set user.password =: password " +
				"where user.id =: id");
		query.setParameter("id", id);
		query.setParameter("password", password);
		query.executeUpdate();
	}
}
