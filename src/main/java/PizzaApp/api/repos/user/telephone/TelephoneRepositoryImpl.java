package PizzaApp.api.repos.user.telephone;

import PizzaApp.api.entity.dto.user.TelephoneDTO;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;

import java.util.List;

@Repository
public class TelephoneRepositoryImpl implements TelephoneRepository {

	private final EntityManager em;

	public TelephoneRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<TelephoneDTO> findAllByUserId(Long userId) {
		return em.createQuery("""
							select new TelephoneDTO(
							telehpone.id,
							telehpone.number
							)
							from Telephone telehpone
							where telehpone.userData.id = :userId
						""", TelephoneDTO.class)
				.setParameter("userId", userId)
				.getResultList();
	}

	@Override
	public Long findUserTelListSize(Long userId) {
		return (Long) em.createQuery("select count(t) from Telephone t where t.userData.id = :userId")
				.setParameter("userId", userId).getSingleResult();
	}
}