package PizzaApp.api.repos;

import java.util.List;
import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class ProductRepository {

	private EntityManager em;

	public ProductRepository(EntityManager em) {
		this.em = em;
	}

	public List<Product> getProducts(String productType, String format) {
		if (format != null) {
			// in FROM Pizza => Pizza is the Entity class name not the table name
			// and in WHERE clause it must be class property not column name
			TypedQuery<Product> query = em.createQuery("FROM Product WHERE productType=:productType AND format=:format",
					Product.class);

			query.setParameter("productType", productType);
			query.setParameter("format", format);
			List<Product> products = query.getResultList();
			return products;
		} else {
			TypedQuery<Product> query = em.createQuery("FROM Product WHERE productType=:productType", Product.class);
			query.setParameter("productType", productType);
			List<Product> products = query.getResultList();
			return products;
		}
	}
}
