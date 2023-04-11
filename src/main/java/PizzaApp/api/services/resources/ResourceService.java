package PizzaApp.api.services.resources;

import java.util.List;
import org.springframework.stereotype.Service;

import PizzaApp.api.entity.resources.Offer;
import PizzaApp.api.entity.resources.Product;
import PizzaApp.api.entity.resources.Store;
import PizzaApp.api.repos.resources.OfferRepository;
import PizzaApp.api.repos.resources.ProductRepository;
import PizzaApp.api.repos.resources.StoreRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ResourceService {

	private ProductRepository productRepository;
	private StoreRepository storeRepository;
	private OfferRepository offerRepository;

	public ResourceService(ProductRepository productRepository, StoreRepository storeRepository,
			OfferRepository offerRepository) {
		this.productRepository = productRepository;
		this.storeRepository = storeRepository;
		this.offerRepository = offerRepository;
	}

	public List<Product> getProducts(String productType) {
		return productRepository.getProducts(productType);
	}

	public List<Store> getAllStores() {
		return storeRepository.getAllStores();
	}

	public List<Offer> findAllOffers() {
		return offerRepository.findAll();
	}
}
