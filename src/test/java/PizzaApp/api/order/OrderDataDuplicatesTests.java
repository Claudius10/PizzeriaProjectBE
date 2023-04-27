package PizzaApp.api.order;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import PizzaApp.api.entity.clients.customer.Customer;
import PizzaApp.api.entity.clients.customer.Telephone;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class OrderDataDuplicatesTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private List<Telephone> telList;
	private List<Customer> customerList;
	private List<Address> addressList;

	@BeforeAll
	void setup() {

		// create telephone list

		Telephone firstTel = new Telephone(666333999);
		Telephone secondTel = new Telephone(666333666);
		Telephone thirdTel = new Telephone(666999333);

		Telephone[] telArray = { firstTel, secondTel, thirdTel };
		telList = new ArrayList<>();
		Collections.addAll(telList, telArray);

		// create customer list

		Customer firstCustomer = new Customer("FirstCustomer", "FirstTel", "firstTest@email.com", firstTel);
		Customer secondCustomer = new Customer("SecondCustomer", "SecondTel", "secondTest@email.com", secondTel);
		Customer thirdCustomer = new Customer("ThirdCustomer", "FirstTel", "testphone@email.com", firstTel);

		Customer newCustomerNewTel = new Customer("newCustomerNewTel", "AllNew", "newCustomerNewTel@email.com",
				thirdTel);

		Customer[] customerArray = { firstCustomer, secondCustomer, thirdCustomer, newCustomerNewTel };
		customerList = new ArrayList<>();
		Collections.addAll(customerList, customerArray);

		// create address list

		Address firstAddress = new Address("testAddress", 5, "", "", "15", "5");
		Address secondAddress = new Address("testerinoAddressino", 33, "", "", "9", "6");
		Address originalAddress = new Address("originalAddress", 5, "", "", "15", "5");
		Address newAddress = new Address("newAddress", 33, "", "", "9", "6");

		Address[] addressArray = { firstAddress, secondAddress, originalAddress, newAddress };
		addressList = new ArrayList<>();
		Collections.addAll(addressList, addressArray);

	}

	@Test
	@DisplayName("Test for ensuring there are no duplicate telephones")
	public void givenTelephones_whenFindByNumber_thenReturnNoDuplicateTelephones()
			throws JsonProcessingException, Exception {

		// find tel entries that matche given tel

		System.out.println("-------- Checking telephone entries in database --------");
		for (Telephone telephone : telList) {
			ResultActions telephoneResponse = mockMvc
					.perform(get("/api/telephone/test/{number}", telephone.getNumber()));

			telephoneResponse.andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));

			System.out.println("No duplicate telephones found with the number: " + telephone.getNumber());
		}
		System.out.println("-------- No duplicate telephones found --------");

	}

	@Test
	@DisplayName("Test for ensuring there are no duplicate customers")
	public void givenCustomers_whenFindCustomers_thenReturnNoDuplicateCustomers()
			throws JsonProcessingException, Exception {

		// find customer entries that matche given customer

		System.out.println("-------- Checking customer entries in database --------");
		for (Customer customer : customerList) {
			ResultActions customerResponse = mockMvc.perform(get("/api/customer")
					.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(customer)));

			customerResponse.andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));

			System.out.println("No duplicates found for customer with tel: " + customer.getTel().getNumber());
		}
		System.out.println("-------- No duplicate customers found --------");

	}

	@Test
	@DisplayName("Test for ensuring there are no duplicate addresses")
	public void givenAddresses_whenFindAddresses_thenReturnNoDuplicateAddresses()
			throws JsonProcessingException, Exception {

		// find address entries that match given address

		System.out.println("-------- Checking address entries in database --------");
		for (Address address : addressList) {
			ResultActions addressResponse = mockMvc.perform(get("/api/address").contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(address)));

			addressResponse.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));

			System.out.println("No duplicates found for address: " + address);
		}
		System.out.println("-------- No duplicate addresses found --------");
	}
}
