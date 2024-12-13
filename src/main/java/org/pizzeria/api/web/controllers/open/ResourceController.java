package org.pizzeria.api.web.controllers.open;

import org.pizzeria.api.services.resources.ResourceService;
import org.pizzeria.api.services.store.StoreService;
import org.pizzeria.api.web.dto.api.Response;
import org.pizzeria.api.web.globals.ApiRoutes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.BASE + ApiRoutes.V1 + ApiRoutes.RESOURCE_BASE)
public class ResourceController {

	private final ResourceService resourceService;

	private final StoreService storeService;

	public ResourceController(ResourceService resourceService, StoreService storeService) {
		this.resourceService = resourceService;
		this.storeService = storeService;
	}

	@GetMapping(path = ApiRoutes.RESOURCE_PRODUCT, params = ApiRoutes.RESOURCE_PRODUCT_PARAM)
	public ResponseEntity<Response> findAllByType(@RequestParam String type) {

		Response response = Response.builder()
				.statusCode(HttpStatus.OK.value())
				.statusDescription(HttpStatus.OK.name())
				.data(resourceService.findAllProductsByType(type))
				.build();

		return ResponseEntity.ok(response);
	}

	@GetMapping(ApiRoutes.RESOURCE_STORE)
	public ResponseEntity<Response> findAllStores() {

		Response response = Response.builder()
				.statusCode(HttpStatus.OK.value())
				.statusDescription(HttpStatus.OK.name())
				.data(storeService.findAll())
				.build();

		return ResponseEntity.ok(response);
	}

	@GetMapping(ApiRoutes.RESOURCE_OFFER)
	public ResponseEntity<Response> findAllOffers() {

		Response response = Response.builder()
				.statusCode(HttpStatus.OK.value())
				.statusDescription(HttpStatus.OK.name())
				.data(resourceService.findAllOffers())
				.build();

		return ResponseEntity.ok(response);
	}
}