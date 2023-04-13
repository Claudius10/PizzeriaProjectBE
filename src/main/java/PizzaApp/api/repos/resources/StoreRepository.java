package PizzaApp.api.repos.resources;

import java.util.List;
import org.springframework.stereotype.Repository;

import PizzaApp.api.entity.resources.Store;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class StoreRepository {

	private EntityManager em;

	public StoreRepository(EntityManager em) {
		this.em = em;
	}

	public List<Store> findAll() {
		TypedQuery<Store> query = em.createQuery("FROM Store", Store.class);
		List<Store> stores = query.getResultList();
		return stores;
	}
}
