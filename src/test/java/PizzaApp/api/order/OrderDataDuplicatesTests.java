package PizzaApp.api.order;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import PizzaApp.api.entity.clients.Address;
import PizzaApp.api.entity.clients.Email;

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
	private List<Email> emailList;
	private List<Address> addressList;

	@BeforeAll
	void setup() {

		// create email list

		Email firstEmail = new Email("firstEmail@email.com");
		Email secondEmail = new Email("secondEmail@email.com");
		Email thirdEmail = new Email("originalEmail@email.com");
		Email fourthEmail = new Email("NewEmail@email.com");

		Email[] emailArray = { firstEmail, secondEmail, thirdEmail, fourthEmail };
		emailList = new ArrayList<>();
		Collections.addAll(emailList, emailArray);

		// create address list

		Address firstAddress = new Address("FirstAddress", 5, "", "", "15", "5");
		Address secondAddress = new Address("SecondAddress", 33, "", "", "9", "6");
		Address thirdAddress = new Address("OriginalAddress", 12, "", "", "1", "3");
		Address fourthAddress = new Address("NewAddress", 33, "", "", "9", "6");

		Address[] addressArray = { firstAddress, secondAddress, thirdAddress, fourthAddress };
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
	@DisplayName("Duplicates test #1: no duplicate addresses")
	public void givenAddresses_whenFindAddresses_thenReturnNoDuplicateAddresses()
			throws JsonProcessingException, Exception {

		logger.info("Duplicates test #1: Checking for duplicate addresses in DB");
		for (Address address : addressList) {

			// send a get to findAddress for every address obj
			ResultActions addressResponse = mockMvc.perform(get("/api/address").contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(address)));

			// check for every obj that no exception is thrown
			// it there is, then it means there is a duplicate
			assertDoesNotThrow(() -> {
				addressResponse.andExpect(status().isOk()).andExpect(jsonPath("$.street", is(address.getStreet())))
						.andExpect(jsonPath("$.streetNr", is(address.getStreetNr())))
						.andExpect(jsonPath("$.gate", is(address.getGate())))
						.andExpect(jsonPath("$.staircase", is(address.getStaircase())))
						.andExpect(jsonPath("$.floor", is(address.getFloor())))
						.andExpect(jsonPath("$.door", is(address.getDoor())));
			});
		}
		logger.info("Duplicates test #1: Success, no duplicate addresses found");
	}

	@Test
	@DisplayName("Duplicates test #2: no duplicate emails")
	public void givenEmails_whenFindEmails_thenReturnNoDuplicateEmails() throws JsonProcessingException, Exception {

		logger.info("Duplicates test #2: Checking for duplicate emails in DB");
		for (Email email : emailList) {

			// send a get to findByAddress for every email obj
			ResultActions emailResponse = mockMvc.perform(get("/api/email").contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(email)));

			// check for every obj that no exception is thrown
			// it there is, then it means there is a duplicate
			assertDoesNotThrow(() -> {
				emailResponse.andExpect(status().isOk()).andExpect(jsonPath("$.email", is(email.getEmail())));
			});

		}
		logger.info("Duplicates test #2: Success, no duplicate emails found");
	}

	/*
	 * this test is decommissioned for now
	 * 
	 * @Test
	 * 
	 * @DisplayName("Test for ensuring there are no duplicate telephones") public
	 * void givenTelephones_whenFindByNumber_thenReturnNoDuplicateTelephones()
	 * throws JsonProcessingException, Exception {
	 * 
	 * // find tel entries that matche given tel
	 * 
	 * System.out.println("-------- Checking telephone entries in database --------"
	 * ); for (Telephone telephone : telList) { ResultActions telephoneResponse =
	 * mockMvc .perform(get("/api/telephone/test/{number}", telephone.getNumber()));
	 * 
	 * telephoneResponse.andExpect(status().isOk()).andExpect(jsonPath("$.length()",
	 * is(1)));
	 * 
	 * System.out.println("No duplicate telephones found with the number: " +
	 * telephone.getNumber()); }
	 * System.out.println("-------- No duplicate telephones found --------");
	 * 
	 * }
	 */

}
