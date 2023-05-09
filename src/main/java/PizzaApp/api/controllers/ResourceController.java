package PizzaApp.api.controllers;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@CrossOrigin(origins = "https://pizzeria-project-claudius10.vercel.app/")
public class ResourceController {

	private ResourceService resourceService;

	public ResourceController(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	@GetMapping("/products/{productType}")
	public ResponseEntity<List<Product>> findAllProductsByType(@PathVariable String productType) {
		return new ResponseEntity<List<Product>>(resourceService.findAllProductsByType(productType), HttpStatus.ACCEPTED);
	}

	@GetMapping("/stores")
	public ResponseEntity<List<Store>> findAllStores() {
		return new ResponseEntity<List<Store>>(resourceService.findAllStores(), HttpStatus.ACCEPTED);
	}

	@GetMapping("/offers")
	public ResponseEntity<List<Offer>> findAllOffers() {
		return new ResponseEntity<List<Offer>>(resourceService.findAllOffers(), HttpStatus.ACCEPTED);
	}
}
