package PizzaApp.api.controllers.open.common;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import PizzaApp.api.entity.resources.Offer;
import PizzaApp.api.entity.resources.Product;
import PizzaApp.api.entity.resources.Store;
import PizzaApp.api.services.common.resources.ResourceService;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

	private final ResourceService resourceService;

	public ResourceController(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	@GetMapping("/product/{productType}")
	public ResponseEntity<List<Product>> findAllProductsByType(@PathVariable String productType) {
		return new ResponseEntity<>(resourceService.findAllProductsByType(productType),
				HttpStatus.ACCEPTED);
	}

	@GetMapping("/store")
	public ResponseEntity<List<Store>> findAllStores() {
		return new ResponseEntity<>(resourceService.findAllStores(), HttpStatus.ACCEPTED);
	}

	@GetMapping("/offer")
	public ResponseEntity<List<Offer>> findAllOffers() {
		return new ResponseEntity<>(resourceService.findAllOffers(), HttpStatus.ACCEPTED);
	}
}
