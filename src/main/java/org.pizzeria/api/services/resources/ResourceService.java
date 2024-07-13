package org.pizzeria.api.services.resources;

import org.pizzeria.api.entity.resources.Offer;
import org.pizzeria.api.entity.resources.Product;

import java.util.List;

public interface ResourceService {

	List<Product> findAllProductsByType(String productType);

	List<Offer> findAllOffers();
}