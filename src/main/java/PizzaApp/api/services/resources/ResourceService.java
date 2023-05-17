package PizzaApp.api.services.resources;
import java.util.List;
import PizzaApp.api.entity.resources.Offer;
import PizzaApp.api.entity.resources.Product;
import PizzaApp.api.entity.resources.Store;

public interface ResourceService {
	
	public List<Product> findAllProductsByType(String productType);
	
	public List<Store> findAllStores();
	
	public List<Offer> findAllOffers();

}
