package org.pizzeria.api.controller.open;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.entity.resources.Offer;
import org.pizzeria.api.entity.resources.Product;
import org.pizzeria.api.entity.resources.Store;
import org.pizzeria.api.repos.address.AddressRepository;
import org.pizzeria.api.repos.resources.OfferRepository;
import org.pizzeria.api.repos.resources.ProductRepository;
import org.pizzeria.api.services.store.StoreService;
import org.pizzeria.api.web.dto.api.Response;
import org.pizzeria.api.web.globals.ApiRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.pizzeria.api.utils.TestUtils.getResponse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@DirtiesContext
class ResourceControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OfferRepository offerRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private StoreService storeService;

	@BeforeAll
	void data() {
		productRepository.save(new Product(null, "pizza", null, "", "", 1D, ""));
		offerRepository.save(new Offer());
		addressRepository.save(new Address.Builder().withStreet("Street").withNumber(5).build());
		storeService.createStore(1L, "", null, "", "");
	}

	@Test
	void givenGetProductApiCall_thenReturnResource() throws Exception {
		// Act

		// get api call to find product list
		MockHttpServletResponse response = mockMvc.perform(get(
						ApiRoutes.BASE +
								ApiRoutes.V1 +
								ApiRoutes.RESOURCE_BASE +
								ApiRoutes.RESOURCE_PRODUCT + "?type=pizza"))
				.andReturn().getResponse();

		// Assert

		Response responseObj = getResponse(response, objectMapper);
		List<Product> productList = objectMapper.convertValue(responseObj.getData(), List.class);
		assertThat(productList).hasSize(1);
	}

	@Test
	void givenGetStoresApiCall_thenReturnResource() throws Exception {
		// Act

		// get api call to find store list
		MockHttpServletResponse response = mockMvc.perform(get(
				ApiRoutes.BASE +
						ApiRoutes.V1 +
						ApiRoutes.RESOURCE_BASE +
						ApiRoutes.RESOURCE_STORE
		)).andReturn().getResponse();

		// Assert

		Response responseObj = getResponse(response, objectMapper);
		List<Store> storeList = objectMapper.convertValue(responseObj.getData(), List.class);
		assertThat(storeList).hasSize(1);
	}

	@Test
	void givenGetOffersApiCall_thenReturnResource() throws Exception {
		// Act

		// get api call to find offer list
		MockHttpServletResponse response = mockMvc.perform(get(ApiRoutes.BASE +
				ApiRoutes.V1 +
				ApiRoutes.RESOURCE_BASE +
				ApiRoutes.RESOURCE_OFFER
		)).andReturn().getResponse();

		// Assert

		Response responseObj = getResponse(response, objectMapper);
		List<Offer> offerList = objectMapper.convertValue(responseObj.getData(), List.class);
		assertThat(offerList).hasSize(1);
	}
}