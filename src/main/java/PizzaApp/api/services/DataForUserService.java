package PizzaApp.api.services;

import java.util.List;
import org.springframework.stereotype.Service;
import PizzaApp.api.entity.Product;
import PizzaApp.api.entity.Store;
import PizzaApp.api.repos.ProductRepository;
import PizzaApp.api.repos.StoreRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class DataForUserService {

	private ProductRepository productRepository;
	private StoreRepository storeRepository;
	// private OfferRepository offerRepository;

	public DataForUserService(ProductRepository productRepository, StoreRepository storeRepository) {
		this.productRepository = productRepository;
		this.storeRepository = storeRepository;
	}

	public List<Product> getProducts(String productType, String format) {
		return productRepository.getProducts(productType, format);
	}

	public List<Store> getAllStores() {
		return storeRepository.getAllStores();
	}
}
