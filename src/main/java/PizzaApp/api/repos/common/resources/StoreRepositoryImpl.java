package PizzaApp.api.repos.common.resources;

import java.util.List;

import org.springframework.stereotype.Repository;

import PizzaApp.api.entity.resources.Store;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class StoreRepositoryImpl implements StoreRepository {

	private final EntityManager em;

	public StoreRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<Store> findAll() {
		TypedQuery<Store> query = em.createQuery("from Store st JOIN FETCH st.address add ", Store.class);
		return query.getResultList();
	}
}
