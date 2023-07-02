package PizzaApp.api.repos.common.resources;

import java.util.List;

import PizzaApp.api.entity.resources.Product;

public interface ProductRepository {

	List<Product> findAllByType(String productType);
}
