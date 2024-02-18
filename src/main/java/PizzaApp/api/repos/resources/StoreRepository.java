package PizzaApp.api.repos.resources;

import PizzaApp.api.entity.resources.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

	@Query("from Store store join fetch store.address")
	List<Store> findAllStores();
}
