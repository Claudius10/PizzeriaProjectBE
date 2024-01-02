package PizzaApp.api.repos.user.account;

import PizzaApp.api.entity.user.UserData;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class UserDataRepositoryImpl implements UserDataRepository {

	private final EntityManager em;

	public UserDataRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public void createData(UserData userData) {
		em.persist(userData);
	}

	@Override
	public UserData findReference(Long userId) {
		return em.getReference(UserData.class, userId);
	}

	@Override
	public UserData findById(Long userId) {
		return em.find(UserData.class, userId);
	}

	@Override
	public void delete(Long userId) {
		// delete user data (cascades to user)
		em.remove(findReference(userId));
	}
}
