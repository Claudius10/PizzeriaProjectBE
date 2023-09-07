package PizzaApp.api.repos.user.telephone;

import PizzaApp.api.entity.dto.user.TelephoneDTO;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

@Repository
public class TelephoneRepositoryImpl implements TelephoneRepository {

	private final EntityManager em;

	public TelephoneRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public Optional<List<TelephoneDTO>> findAllByUserId(String id) {
		TypedQuery<TelephoneDTO> query = em.createQuery("""
					select new TelephoneDTO(
					t.id,
					t.number)
					from Telephone t
					where t.userData.id = :userId
				""", TelephoneDTO.class);
		query.setParameter("userId", id);
		return Optional.of(query.getResultList());
	}

	@Override
	public Long findUserTelListSize(String id) {
		return (Long) em.createQuery("select count(t) from Telephone t where t.userData.id = :userId")
				.setParameter("userId", id).getSingleResult();
	}
}