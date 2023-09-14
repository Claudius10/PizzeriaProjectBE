package PizzaApp.api.repos.common.resources;

import java.util.List;

import org.springframework.stereotype.Repository;

import PizzaApp.api.entity.resources.Store;
import jakarta.persistence.EntityManager;

@Repository
public class StoreRepositoryImpl implements StoreRepository {

	private final EntityManager em;

	public StoreRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<Store> findAll() {
		return em.createQuery("select store from Store store JOIN FETCH store.address", Store.class).getResultList();
	}
}
