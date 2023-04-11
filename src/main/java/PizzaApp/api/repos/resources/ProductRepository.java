package PizzaApp.api.repos.resources;

import java.util.List;
import org.springframework.stereotype.Repository;

import PizzaApp.api.entity.resources.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class ProductRepository {

	private EntityManager em;

	public ProductRepository(EntityManager em) {
		this.em = em;
	}

	public List<Product> getProducts(String productType) {
		TypedQuery<Product> query = em.createQuery("FROM Product WHERE productType=:productType", Product.class);
		query.setParameter("productType", productType);
		List<Product> products = query.getResultList();
		return products;
	}
}