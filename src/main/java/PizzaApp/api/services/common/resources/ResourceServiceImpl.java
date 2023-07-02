package PizzaApp.api.services.common.resources;

import java.util.List;

import org.springframework.stereotype.Service;
import PizzaApp.api.entity.resources.Offer;
import PizzaApp.api.entity.resources.Product;
import PizzaApp.api.entity.resources.Store;
import PizzaApp.api.repos.common.resources.OfferRepository;
import PizzaApp.api.repos.common.resources.ProductRepository;
import PizzaApp.api.repos.common.resources.StoreRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

	private final ProductRepository productRepository;
	private final StoreRepository storeRepository;
	private final OfferRepository offerRepository;

	public ResourceServiceImpl(ProductRepository productRepository, StoreRepository storeRepository,
							   OfferRepository offerRepository) {
		this.productRepository = productRepository;
		this.storeRepository = storeRepository;
		this.offerRepository = offerRepository;
	}

	@Override
	public List<Product> findAllProductsByType(String productType) {
		return productRepository.findAllByType(productType);
	}

	@Override
	public List<Store> findAllStores() {
		return storeRepository.findAll();
	}

	@Override
	public List<Offer> findAllOffers() {
		return offerRepository.findAll();
	}
}
