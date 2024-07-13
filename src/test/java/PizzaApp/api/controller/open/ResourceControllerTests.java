package PizzaApp.api.controller.open;

import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.resources.Offer;
import PizzaApp.api.entity.resources.Product;
import PizzaApp.api.entity.resources.Store;
import PizzaApp.api.repos.address.AddressRepository;
import PizzaApp.api.repos.resources.OfferRepository;
import PizzaApp.api.repos.resources.ProductRepository;
import PizzaApp.api.services.store.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
		addressRepository.save(new Address.Builder().withStreet("Street").withStreetNr(5).build());
		storeService.createStore(1L, "", null, "");
	}

	@Test
	void givenGetProductApiCall_thenReturnResource() throws Exception {
		// Act

		// get api call to find product list
		String response = mockMvc.perform(get("/api/resource/product?type=pizza")).andReturn().getResponse().getContentAsString();

		// Assert

		List<Product> productList = Arrays.asList(objectMapper.readValue(response, Product[].class));
		assertThat(productList).hasSize(1);
	}

	@Test
	void givenGetStoresApiCall_thenReturnResource() throws Exception {
		// Act

		// get api call to find store list
		String response = mockMvc.perform(get("/api/resource/store")).andReturn().getResponse().getContentAsString();

		// Assert

		List<Store> storeList = Arrays.asList(objectMapper.readValue(response, Store[].class));
		assertThat(storeList).hasSize(1);
	}

	@Test
	void givenGetOffersApiCall_thenReturnResource() throws Exception {
		// Act

		// get api call to find offer list
		String response = mockMvc.perform(get("/api/resource/offer")).andReturn().getResponse().getContentAsString();

		// Assert

		List<Offer> offerList = Arrays.asList(objectMapper.readValue(response, Offer[].class));
		assertThat(offerList).hasSize(1);
	}
}