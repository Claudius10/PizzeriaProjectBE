package PizzaApp.api.controllers.open;

import PizzaApp.api.entity.resources.Offer;
import PizzaApp.api.entity.resources.Product;
import PizzaApp.api.entity.resources.Store;
import PizzaApp.api.services.resources.ResourceService;
import PizzaApp.api.services.store.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

	private final ResourceService resourceService;

	private final StoreService storeService;

	public ResourceController(ResourceService resourceService, StoreService storeService) {
		this.resourceService = resourceService;
		this.storeService = storeService;
	}

	@GetMapping(path = "/product", params = "type")
	public ResponseEntity<List<Product>> findAllByType(@RequestParam String type) {
		return ResponseEntity.ok().body(resourceService.findAllProductsByType(type));
	}

	@GetMapping("/store")
	public ResponseEntity<List<Store>> findAllStores() {
		return ResponseEntity.ok().body(storeService.findAll());
	}

	@GetMapping("/offer")
	public ResponseEntity<List<Offer>> findAllOffers() {
		return ResponseEntity.ok().body(resourceService.findAllOffers());
	}
}