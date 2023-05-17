package PizzaApp.api.repos.resources;
import java.util.List;
import PizzaApp.api.entity.resources.Product;

public interface ProductRepository {

	public List<Product> findAllByType(String productType);
}
