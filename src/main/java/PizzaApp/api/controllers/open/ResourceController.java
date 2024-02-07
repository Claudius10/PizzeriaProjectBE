package PizzaApp.api.controllers.open;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import PizzaApp.api.entity.resources.Offer;
import PizzaApp.api.entity.resources.Product;
import PizzaApp.api.entity.resources.Store;
import PizzaApp.api.services.resources.ResourceService;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

	private final ResourceService resourceService;

	public ResourceController(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	@GetMapping(path = "/product", params = "type")
	public ResponseEntity<List<Product>> findAllByType(@RequestParam String type) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(resourceService.findAllProductsByType(type));
	}

	@GetMapping("/store")
	public ResponseEntity<List<Store>> findAllStores() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(resourceService.findAllStores());
	}

	@GetMapping("/offer")
	public ResponseEntity<List<Offer>> findAllOffers() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(resourceService.findAllOffers());
	}
}
