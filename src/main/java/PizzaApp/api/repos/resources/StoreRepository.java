package PizzaApp.api.repos.resources;
import java.util.List;
import PizzaApp.api.entity.resources.Store;

public interface StoreRepository {
	
	public List<Store> findAll();
}
