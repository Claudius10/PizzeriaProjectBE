package PizzaApp.api.controller;

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
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class ResourceControllerTests {

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
	public void data() {
		productRepository.save(new Product(null, "pizza", null, "", "", 1D, ""));
		offerRepository.save(new Offer());
		addressRepository.save(new Address.Builder().withStreet("Street").withStreetNr(5).build());
		storeService.createStore(1L, "", null, "");
	}

	@Test
	public void givenGetProductApiCall_thenReturnResource() throws Exception {
		String response = mockMvc.perform(get("/api/resource/product?type=pizza")).andReturn().getResponse().getContentAsString();
		List<Product> productList = Arrays.asList(objectMapper.readValue(response, Product[].class));
		assertThat(productList.size()).isEqualTo(1);
	}

	@Test
	public void givenGetStoresApiCall_thenReturnResource() throws Exception {
		String response = mockMvc.perform(get("/api/resource/store")).andReturn().getResponse().getContentAsString();
		List<Store> storeList = Arrays.asList(objectMapper.readValue(response, Store[].class));
		assertThat(storeList.size()).isEqualTo(1);
	}

	@Test
	public void givenGetOffersApiCall_thenReturnResource() throws Exception {
		String response = mockMvc.perform(get("/api/resource/offer")).andReturn().getResponse().getContentAsString();
		List<Offer> offerList = Arrays.asList(objectMapper.readValue(response, Offer[].class));
		assertThat(offerList.size()).isEqualTo(1);
	}
}