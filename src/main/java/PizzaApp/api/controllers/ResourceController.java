package PizzaApp.api.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PizzaApp.api.entity.resources.Offer;
import PizzaApp.api.entity.resources.Product;
import PizzaApp.api.entity.resources.Store;
import PizzaApp.api.services.resources.ResourceService;

@RestController
@RequestMapping("/api/data")
@CrossOrigin(origins = "http://192.168.1.11:3000")
public class ResourceController {

	private ResourceService resourceService;

	public ResourceController(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	@GetMapping("/products/{productType}")
	public List<Product> getProducts(@PathVariable String productType) {
		return resourceService.getProducts(productType);
	}

	@GetMapping("/stores")
	public List<Store> getAllStores() {
		return resourceService.getAllStores();
	}

	@GetMapping("/offers")
	public List<Offer> findAllOffers() {
		return resourceService.findAllOffers();
	}
}
