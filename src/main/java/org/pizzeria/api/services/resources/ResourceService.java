package org.pizzeria.api.services.resources;

import org.pizzeria.api.entity.resources.Offer;
import org.pizzeria.api.entity.resources.Product;
import org.pizzeria.api.entity.resources.Store;

import java.util.List;
import java.util.Optional;

public interface ResourceService {

	Optional<Store> findStoreByAddressId(Long id);

	List<Product> findAllProductsByType(String productType);

	List<Offer> findAllOffers();

	List<Store> findAllStores();
}