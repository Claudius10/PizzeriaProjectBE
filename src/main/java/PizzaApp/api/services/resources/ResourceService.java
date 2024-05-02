package PizzaApp.api.services.resources;

import PizzaApp.api.entity.resources.Offer;
import PizzaApp.api.entity.resources.Product;

import java.util.List;

public interface ResourceService {

	List<Product> findAllProductsByType(String productType);

	List<Offer> findAllOffers();
}