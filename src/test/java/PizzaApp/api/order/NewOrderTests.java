package PizzaApp.api.order;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.databind.ObjectMapper;
import PizzaApp.api.entity.cart.Cart;
import PizzaApp.api.entity.clients.Address;
import PizzaApp.api.entity.clients.customer.Customer;
import PizzaApp.api.entity.clients.customer.Telephone;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.OrderItem;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class NewOrderTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	// test objects
	private Address firstAddress;
	private Address secondAddress;

	private Telephone firstTel;
	private Telephone secondTel;

	private Customer firstCustomer;
	private Customer secondCustomer;
	private Customer thirdCustomer;

	private OrderDetails orderDetails;
	private Cart cart;

	@BeforeAll
	void setup() {

		// address
		firstAddress = new Address("testAddress", 5, "", "", "15", "5");
		secondAddress = new Address("testerinoAddressino", 33, "", "", "9", "6");

		// tel
		firstTel = new Telephone(666333999);
		secondTel = new Telephone(666333666);

		// customer
		firstCustomer = new Customer("FirstCustomer", "FirstTel", "firstTest@email.com", firstTel);
		secondCustomer = new Customer("SecondCustomer", "SecondTel", "secondTest@email.com", secondTel);
		thirdCustomer = new Customer("ThirdCustomer", "FirstTel", "testphone@email.com", firstTel);

		// orderDetails
		orderDetails = new OrderDetails();
		orderDetails.setOrderDate(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH:mm:ss")));
		orderDetails.setDeliveryHour("ASAP");
		orderDetails.setPaymentType("Credit Card");

		// items
		List<OrderItem> orderItems = new ArrayList<>();
		OrderItem testItem = new OrderItem("pizza", "Carbonara", "Mediana", 1, 14.75);
		orderItems.add(testItem);

		// cart
		cart = new Cart();
		cart.setOrderItems(orderItems);
		cart.setTotalCost(14.75);
		cart.setTotalCostOffers(0);
		cart.setTotalQuantity(1);
	}

	@Test
	@DisplayName("Test for creating order with new customer and new address")
	public void givenOrder_whenCreateOrUpdate_thenReturnOrder() throws Exception {

		// given / preparation:

		// order with new customer and new address
		Order order = new Order();
		order.setAddress(firstAddress);
		order.setCustomer(firstCustomer);
		order.setOrderDetails(orderDetails);
		order.setCart(cart);

		// when action:
		// create new order
		ResultActions orderResponse = mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert:
		// the returned order data matches the data set for testing
		System.out.println("-------- Creating order: new customer and new address --------");
		orderResponse.andDo(print()).andExpect(status().isCreated())
				.andExpect(jsonPath("$.customer.firstName", is(firstCustomer.getFirstName())))
				.andExpect(jsonPath("$.customer.lastName", is(firstCustomer.getLastName())))
				.andExpect(jsonPath("$.customer.tel.number", is(firstCustomer.getTel().getNumber())))
				.andExpect(jsonPath("$.customer.email", is(firstCustomer.getEmail())))
				.andExpect(jsonPath("$.address.street", is(firstAddress.getStreet())))
				.andExpect(jsonPath("$.address.streetNr", is(firstAddress.getStreetNr())))
				.andExpect(jsonPath("$.address.gate", is(firstAddress.getGate())))
				.andExpect(jsonPath("$.address.staircase", is(firstAddress.getStaircase())))
				.andExpect(jsonPath("$.address.floor", is(firstAddress.getFloor())))
				.andExpect(jsonPath("$.address.door", is(firstAddress.getDoor())))
				.andExpect(jsonPath("$.orderDetails.deliveryHour", is(orderDetails.getDeliveryHour())))
				.andExpect(jsonPath("$.orderDetails.paymentType", is(orderDetails.getPaymentType())))
				.andExpect(jsonPath("$.cart.totalQuantity", is(1))).andExpect(jsonPath("$.cart.totalCost", is(14.75)))
				.andExpect(jsonPath("$.cart.totalCostOffers", is(0.0)))
				.andExpect(jsonPath("$.cart.orderItems.length()", is(1)));
		System.out.println("-------- Order created: new customer and new address --------");
	}

	@Test
	@DisplayName("Test for creating order with existing customer and new address")
	public void givenOrderWithExistingCustomer_whenCreateOrUpdate_thenUseExistingCustomer() throws Exception {

		// given / preparation:

		// order with existing customer in database and new address
		Order order = new Order();
		order.setAddress(secondAddress);
		order.setCustomer(firstCustomer);
		order.setOrderDetails(orderDetails);
		order.setCart(cart);

		// when action:
		// create new order
		ResultActions orderResponse = mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert:
		// the returned order data matches the data set for testing

		System.out.println("-------- Creating order: existing customer and new address --------");
		orderResponse.andDo(print()).andExpect(status().isCreated())
				.andExpect(jsonPath("$.customer.firstName", is(firstCustomer.getFirstName())))
				.andExpect(jsonPath("$.customer.lastName", is(firstCustomer.getLastName())))
				.andExpect(jsonPath("$.customer.tel.number", is(firstCustomer.getTel().getNumber())))
				.andExpect(jsonPath("$.customer.email", is(firstCustomer.getEmail())))
				.andExpect(jsonPath("$.address.street", is(secondAddress.getStreet())))
				.andExpect(jsonPath("$.address.streetNr", is(secondAddress.getStreetNr())))
				.andExpect(jsonPath("$.address.gate", is(secondAddress.getGate())))
				.andExpect(jsonPath("$.address.staircase", is(secondAddress.getStaircase())))
				.andExpect(jsonPath("$.address.floor", is(secondAddress.getFloor())))
				.andExpect(jsonPath("$.address.door", is(secondAddress.getDoor())))
				.andExpect(jsonPath("$.orderDetails.deliveryHour", is(orderDetails.getDeliveryHour())))
				.andExpect(jsonPath("$.orderDetails.paymentType", is(orderDetails.getPaymentType())))
				.andExpect(jsonPath("$.cart.totalQuantity", is(1))).andExpect(jsonPath("$.cart.totalCost", is(14.75)))
				.andExpect(jsonPath("$.cart.totalCostOffers", is(0.0)))
				.andExpect(jsonPath("$.cart.orderItems.length()", is(1)));
		System.out.println("-------- Order created: existing customer and new address --------");
	}

	@Test
	@DisplayName("Test for creating order with new customer and existing address")
	public void givenOrderWithExistingAddress_whenCreateOrUpdate_thenUseExistingAddress() throws Exception {

		// given / preparation:
		Order order = new Order();
		order.setAddress(firstAddress);
		order.setCustomer(secondCustomer);
		order.setOrderDetails(orderDetails);
		order.setCart(cart);

		// when action:

		// create order with new customer, but with an address already in db
		ResultActions orderResponse = mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert:
		// the returned order data matches with the set data for testing

		System.out.println("-------- Creating order: new customer and existing address --------");
		orderResponse.andDo(print()).andExpect(status().isCreated())
				.andExpect(jsonPath("$.customer.firstName", is(secondCustomer.getFirstName())))
				.andExpect(jsonPath("$.customer.lastName", is(secondCustomer.getLastName())))
				.andExpect(jsonPath("$.customer.tel.number", is(secondCustomer.getTel().getNumber())))
				.andExpect(jsonPath("$.customer.email", is(secondCustomer.getEmail())))
				.andExpect(jsonPath("$.address.street", is(firstAddress.getStreet())))
				.andExpect(jsonPath("$.address.streetNr", is(firstAddress.getStreetNr())))
				.andExpect(jsonPath("$.address.gate", is(firstAddress.getGate())))
				.andExpect(jsonPath("$.address.staircase", is(firstAddress.getStaircase())))
				.andExpect(jsonPath("$.address.floor", is(firstAddress.getFloor())))
				.andExpect(jsonPath("$.address.door", is(firstAddress.getDoor())))
				.andExpect(jsonPath("$.orderDetails.deliveryHour", is(orderDetails.getDeliveryHour())))
				.andExpect(jsonPath("$.orderDetails.paymentType", is(orderDetails.getPaymentType())))
				.andExpect(jsonPath("$.cart.totalQuantity", is(1))).andExpect(jsonPath("$.cart.totalCost", is(14.75)))
				.andExpect(jsonPath("$.cart.totalCostOffers", is(0.0)))
				.andExpect(jsonPath("$.cart.orderItems.length()", is(1)));
		System.out.println("-------- Order created: new customer and existing address --------");
	}

	@Test
	@DisplayName("Test for creating order with new customer, existing telephone, and store pickup")
	public void givenOrderWithExistingTelephone_whenCreateOrUpdate_thenUseExistingTelephone() throws Exception {

		// given / preparation:

		// order with existing telephone in database
		// thirdCustomer has same tel number as firstCustomer, which is already in db
		Order order = new Order();
		order.setStorePickUpName("StoreName");
		order.setCustomer(thirdCustomer);
		order.setOrderDetails(orderDetails);
		order.setCart(cart);

		// when action:
		// create new order
		ResultActions orderResponse = mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert:
		// the returned order data matches the data set for testing

		System.out.println("-------- Creating order: new customer, existing telephone, and store pickup --------");
		orderResponse.andDo(print()).andExpect(status().isCreated())
				.andExpect(jsonPath("$.customer.firstName", is(thirdCustomer.getFirstName())))
				.andExpect(jsonPath("$.customer.lastName", is(thirdCustomer.getLastName())))
				.andExpect(jsonPath("$.customer.tel.number", is(thirdCustomer.getTel().getNumber())))
				.andExpect(jsonPath("$.customer.email", is(thirdCustomer.getEmail())))
				.andExpect(jsonPath("$.storePickUpName", is("StoreName")))
				.andExpect(jsonPath("$.orderDetails.deliveryHour", is(orderDetails.getDeliveryHour())))
				.andExpect(jsonPath("$.orderDetails.paymentType", is(orderDetails.getPaymentType())))
				.andExpect(jsonPath("$.cart.totalQuantity", is(1))).andExpect(jsonPath("$.cart.totalCost", is(14.75)))
				.andExpect(jsonPath("$.cart.totalCostOffers", is(0.0)))
				.andExpect(jsonPath("$.cart.orderItems.length()", is(1)));
		System.out.println("-------- Order created: new customer, existing telephone, and store pickup --------");
	}

	@Test
	@DisplayName("Test for creating order with existing customer data and existing tel, and existing address")
	public void givenOrderWithExistingCustomerWithTelAndAddress_whenCreateOrUpdate_thenUseExistingCustomerAndAddress()
			throws Exception {

		// given / preparation:
		Order order = new Order();
		order.setAddress(firstAddress);
		order.setCustomer(thirdCustomer);
		order.setOrderDetails(orderDetails);
		order.setCart(cart);

		// when action:

		// create order with the customer (and tel) and address already in db
		ResultActions orderResponse = mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert:

		System.out.println(
				"-------- Creating order: existing customer data and existing tel, and existing address --------");
		orderResponse.andDo(print()).andExpect(status().isCreated())
				.andExpect(jsonPath("$.customer.firstName", is(thirdCustomer.getFirstName())))
				.andExpect(jsonPath("$.customer.lastName", is(thirdCustomer.getLastName())))
				.andExpect(jsonPath("$.customer.tel.number", is(thirdCustomer.getTel().getNumber())))
				.andExpect(jsonPath("$.customer.email", is(thirdCustomer.getEmail())))
				.andExpect(jsonPath("$.address.street", is(firstAddress.getStreet())))
				.andExpect(jsonPath("$.address.streetNr", is(firstAddress.getStreetNr())))
				.andExpect(jsonPath("$.address.gate", is(firstAddress.getGate())))
				.andExpect(jsonPath("$.address.staircase", is(firstAddress.getStaircase())))
				.andExpect(jsonPath("$.address.floor", is(firstAddress.getFloor())))
				.andExpect(jsonPath("$.address.door", is(firstAddress.getDoor())))
				.andExpect(jsonPath("$.orderDetails.deliveryHour", is(orderDetails.getDeliveryHour())))
				.andExpect(jsonPath("$.orderDetails.paymentType", is(orderDetails.getPaymentType())))
				.andExpect(jsonPath("$.cart.totalQuantity", is(1))).andExpect(jsonPath("$.cart.totalCost", is(14.75)))
				.andExpect(jsonPath("$.cart.totalCostOffers", is(0.0)))
				.andExpect(jsonPath("$.cart.orderItems.length()", is(1)));
		System.out.println(
				"-------- Order created: existing customer data and existing tel, and existing address --------");
	}
}