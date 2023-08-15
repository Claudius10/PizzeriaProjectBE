package PizzaApp.api.repos.user.telephone;

import PizzaApp.api.entity.dto.user.TelephoneDTO;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.user.Telephone;
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
	public Optional<List<TelephoneDTO>> findByUserId(Long id) {
		TypedQuery<TelephoneDTO> query = em.createQuery("""
					select new TelephoneDTO(
					t.id,
					t.number)
					from Telephone t
					where t.userData.id= :userId
				""", TelephoneDTO.class);
		query.setParameter("userId", id);

		Optional<List<TelephoneDTO>> resultList = Optional.ofNullable(query.getResultList());
		return resultList.isEmpty() ? Optional.empty() : resultList;
	}

	@Override
	public Long findUserTelListSize(Long id) {
		return (Long) em.createQuery("select count(t) from Telephone t where t.userData.id=:userId")
				.setParameter("userId", id).getSingleResult();
	}
}