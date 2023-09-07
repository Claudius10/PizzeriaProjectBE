package PizzaApp.api.order;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import PizzaApp.api.entity.user.Address;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class OrderDataDuplicatesTests {

	private final Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	// private List<Telephone> telList;
	private List<Address> addressList;

	@BeforeAll
	void setup() {

		// create address list
		Address firstAddress = new Address.Builder()
				.withStreet("FirstAddress")
				.withStreetNr(15)
				.withFloor("3")
				.withDoor("2A")
				.build();

		Address secondAddress = new Address.Builder()
				.withStreet("SecondAddress")
				.withStreetNr(33)
				.withStaircase("DER")
				.withFloor("9")
				.withDoor("E")
				.build();

		Address thirdAddress = new Address.Builder()
				.withStreet("OriginalAddress")
				.withStreetNr(12)
				.withStaircase("C")
				.withFloor("11")
				.withDoor("DER")
				.build();

		Address fourthAddress = new Address.Builder()
				.withStreet("NewAddress")
				.withStreetNr(157)
				.build();

		Address[] addressArray = {firstAddress, secondAddress, thirdAddress, fourthAddress};
		addressList = new ArrayList<>();
		Collections.addAll(addressList, addressArray);

		/*
		 * decommissioned for now // create telephone list
		 *
		 * Telephone firstTel = new Telephone(666333999); Telephone secondTel = new
		 * Telephone(666333666); Telephone thirdTel = new Telephone(666999333);
		 *
		 * Telephone[] telArray = { firstTel, secondTel, thirdTel }; telList = new
		 * ArrayList<>(); Collections.addAll(telList, telArray);
		 */
	}

	@Test
	public void givenAddresses_whenFindAddresses_thenDontThrowDueToDuplicates() {
		logger.info("Duplicates test #1: Checking for duplicate addresses in DB");
		assertDoesNotThrow(() -> {
			for (Address address : addressList) {
				// send a get to findAddress for every address obj
				// if there's a duplicate, IncorrectResultSizeDataAccessException
				// will be auto thrown by Spring
				mockMvc.perform(get("/api/address")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(address)));
			}
		});
		logger.info("Duplicates test #1: Success, no duplicate addresses found");
	}
}
