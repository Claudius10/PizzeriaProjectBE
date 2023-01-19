package PizzaApp.api.repos;

import java.util.List;

import org.springframework.stereotype.Repository;

import PizzaApp.api.entity.Store;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class StoreRepository {

	private EntityManager em;

	public StoreRepository(EntityManager em) {
		this.em = em;
	}

	public List<Store> getAllStores() {
		TypedQuery<Store> query = em.createQuery("FROM Store", Store.class);
		List<Store> stores = query.getResultList();
		return stores;
	}

}
