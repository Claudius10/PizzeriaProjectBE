package org.pizzeria.api.services.resources;

import jakarta.transaction.Transactional;
import org.pizzeria.api.entity.resources.Offer;
import org.pizzeria.api.entity.resources.Product;
import org.pizzeria.api.repos.resources.OfferRepository;
import org.pizzeria.api.repos.resources.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

	private final ProductRepository productRepository;

	private final OfferRepository offerRepository;

	public ResourceServiceImpl(ProductRepository productRepository, OfferRepository offerRepository) {
		this.productRepository = productRepository;
		this.offerRepository = offerRepository;
	}

	@Override
	public List<Product> findAllProductsByType(String productType) {
		return productRepository.findAllByProductType(productType);
	}

	@Override
	public List<Offer> findAllOffers() {
		return offerRepository.findAll();
	}
}