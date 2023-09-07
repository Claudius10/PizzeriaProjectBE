package PizzaApp.api.repos.user.account;

import PizzaApp.api.entity.dto.user.UserDataDTO;
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
	public UserData findReference(String id) {
		return em.getReference(UserData.class, id);
	}

	@Override
	public UserData findById(String id) {
		return em.find(UserData.class, id);
	}

	@Override
	public UserDataDTO findDTOById(String id) {
		return em.createQuery("""
						select new UserDataDTO(
						ud.id,
						ud.name,
						ud.email
						)
						from User ud
						where ud.id = :userDataId
						""", UserDataDTO.class)
				.setParameter("userDataId", id)
				.getSingleResult();
	}
}
