package PizzaApp.api.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import PizzaApp.api.entity.Product;
import PizzaApp.api.entity.Store;
import PizzaApp.api.services.DataForUserService;

@RestController
@RequestMapping("/api/data")
@CrossOrigin(origins = "http://localhost:3000")
public class DataForUserController {

	private DataForUserService dataForUserService;

	public DataForUserController(DataForUserService dataForUserService) {
		this.dataForUserService = dataForUserService;
	}

	@GetMapping(value = { "/products/{productType}", "/products/{productType}/{format}" })
	public List<Product> getProducts(@PathVariable String productType,
			@PathVariable(name = "format", required = false) String format) {
		return dataForUserService.getProducts(productType, format);
	}

	@GetMapping("/stores")
	public List<Store> getAllStores() {
		return dataForUserService.getAllStores();
	}
}
