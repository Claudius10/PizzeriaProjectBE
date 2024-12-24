package org.pizzeria.api.web.controllers.open;

import lombok.RequiredArgsConstructor;
import org.pizzeria.api.services.resources.ResourceService;
import org.pizzeria.api.web.dto.api.Response;
import org.pizzeria.api.web.dto.api.Status;
import org.pizzeria.api.web.globals.ApiRoutes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRoutes.BASE + ApiRoutes.V1 + ApiRoutes.RESOURCE_BASE)
public class ResourceController {

	private final ResourceService resourceService;

	@GetMapping(path = ApiRoutes.RESOURCE_PRODUCT, params = ApiRoutes.RESOURCE_PRODUCT_PARAM)
	public ResponseEntity<Response> findAllByType(@RequestParam String type) throws InterruptedException {
		Thread.sleep(1200);

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.OK.name())
						.code(HttpStatus.OK.value())
						.isError(false)
						.build())
				.payload(resourceService.findAllProductsByType(type))
				.build();

		return ResponseEntity.ok(response);
	}

	@GetMapping(ApiRoutes.RESOURCE_STORE)
	public ResponseEntity<Response> findAllStores() {

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.OK.name())
						.code(HttpStatus.OK.value())
						.isError(false)
						.build())
				.payload(resourceService.findAllStores())
				.build();

		return ResponseEntity.ok(response);
	}

	@GetMapping(ApiRoutes.RESOURCE_OFFER)
	public ResponseEntity<Response> findAllOffers() {

		Response response = Response.builder()
				.status(Status.builder()
						.description(HttpStatus.OK.name())
						.code(HttpStatus.OK.value())
						.isError(false)
						.build())
				.payload(resourceService.findAllOffers())
				.build();

		return ResponseEntity.ok(response);
	}
}