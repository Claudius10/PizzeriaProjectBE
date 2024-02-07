package PizzaApp.api.repos.resources;

import java.util.List;

import org.springframework.stereotype.Repository;

import PizzaApp.api.entity.resources.Product;
import jakarta.persistence.EntityManager;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

	private final EntityManager em;

	public ProductRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<Product> findAllByType(String productType) {
		return em.createQuery("select product from Product product where product.productType = :productType", Product.class)
				.setParameter("productType", productType)
				.getResultList();
	}
}
