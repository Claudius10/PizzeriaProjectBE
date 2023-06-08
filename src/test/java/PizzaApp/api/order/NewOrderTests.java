package PizzaApp.api.order;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.fasterxml.jackson.databind.ObjectMapper;
import PizzaApp.api.entity.cart.Cart;
import PizzaApp.api.entity.clients.Email;
import PizzaApp.api.entity.clients.Address;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.OrderItem;
import PizzaApp.api.validation.exceptions.EmptyCartException;
import PizzaApp.api.validation.exceptions.InvalidChangeRequestedException;
import PizzaApp.api.validation.exceptions.InvalidContactTelephoneException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class NewOrderTests {

	private final Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	// test objects
	private Address firstAddress;
	private Address secondAddress;

	private Email firstEmail;
	private Email secondEmail;

	private OrderDetails orderDetails;
	private Cart cart;

	@BeforeAll
	void setup() {

		// address
		firstAddress = new Address("FirstAddress", 5, "", "", "15", "5");
		secondAddress = new Address("SecondAddress", 33, "", "", "9", "6");

		// email
		firstEmail = new Email("firstEmail@email.com");
		secondEmail = new Email("secondEmail@email.com");

		// orderDetails
		orderDetails = new OrderDetails();
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
		cart.setTotalCostOffers(0D);
		cart.setTotalQuantity(1);
	}

	@Test
	@DisplayName("New order test #1: create order")
	public void givenOrder_whenCreateOrUpdate_thenReturnOrder() throws Exception {

		// given / preparation:
		Order order = new Order();
		order.setCustomerFirstName("FirstCustomer");
		order.setCustomerLastName("FirstTel");
		order.setContactTel(666333999);
		order.setEmail(firstEmail);
		order.setAddress(firstAddress);
		order.setOrderDetails(orderDetails);
		order.setCart(cart);

		// action: send post request
		ResultActions orderResponse = mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert: returned data matches set data
		logger.info("New order test #1: create order");
		orderResponse.andExpect(status().isCreated())
				.andExpect(jsonPath("$.customerFirstName", is(order.getCustomerFirstName())))
				.andExpect(jsonPath("$.customerLastName", is(order.getCustomerLastName())))
				.andExpect(jsonPath("$.contactTel", is(order.getContactTel())))
				.andExpect(jsonPath("$.email.email", is(order.getEmail().getEmail())))
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
		logger.info("New order test #1: successfully created order");
	}

	@Test
	@DisplayName("New order test #2: create order with existing email and new address")
	public void givenOrderWithExistingEmail_whenCreateOrUpdate_thenUseExistingEmail() throws Exception {

		// given / preparation:
		Order order = new Order();
		order.setCustomerFirstName("FirstCustomer");
		order.setCustomerLastName("FirstTel");
		order.setContactTel(666333999);
		order.setEmail(firstEmail);
		order.setAddress(secondAddress);
		order.setOrderDetails(orderDetails);
		order.setCart(cart);

		// action: send post request
		ResultActions orderResponse = mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert: returned data matches set data
		logger.info("New order test #2: create order with existing email and new address");
		orderResponse.andExpect(status().isCreated())
				.andExpect(jsonPath("$.customerFirstName", is(order.getCustomerFirstName())))
				.andExpect(jsonPath("$.customerLastName", is(order.getCustomerLastName())))
				.andExpect(jsonPath("$.contactTel", is(order.getContactTel())))
				.andExpect(jsonPath("$.email.email", is(order.getEmail().getEmail())))
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
		logger.info("New order test #2: successfully created order with existing email and new address");
	}

	@Test
	@DisplayName("New order test #3: create order with new email and existing address")
	public void givenOrderWithExistingAddress_whenCreateOrUpdate_thenUseExistingAddress() throws Exception {

		// given / preparation:
		Order order = new Order();
		order.setCustomerFirstName("SecondCustomer");
		order.setCustomerLastName("SecondTel");
		order.setContactTel(123456789);
		order.setEmail(secondEmail);
		order.setAddress(firstAddress);
		order.setOrderDetails(orderDetails);
		order.setCart(cart);

		// action: send post request
		ResultActions orderResponse = mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert: returned data matches set data
		logger.info("New order test #3: create order with new email and existing address");
		orderResponse.andExpect(status().isCreated())
				.andExpect(jsonPath("$.customerFirstName", is(order.getCustomerFirstName())))
				.andExpect(jsonPath("$.customerLastName", is(order.getCustomerLastName())))
				.andExpect(jsonPath("$.contactTel", is(order.getContactTel())))
				.andExpect(jsonPath("$.email.email", is(order.getEmail().getEmail())))
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
		logger.info("New order test #3: successfully created order with new email and existing address");
	}

	@Test
	@DisplayName("New order test #4: create order with existing email and existing address")
	public void givenOrderWithExistingEmailAndAddress_whenCreateOrUpdate_thenUseExistingEmailAndAddress()
			throws Exception {

		// given / preparation:
		Order order = new Order();
		order.setCustomerFirstName("FirstCustomer");
		order.setCustomerLastName("FirstTel");
		order.setContactTel(666333999);
		order.setEmail(firstEmail);
		order.setAddress(firstAddress);
		order.setOrderDetails(orderDetails);
		order.setCart(cart);

		// action: send post request
		ResultActions orderResponse = mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert: returned data matches set data
		logger.info("New order test #4: create order with existing email and existing address");
		orderResponse.andExpect(status().isCreated())
				.andExpect(jsonPath("$.customerFirstName", is(order.getCustomerFirstName())))
				.andExpect(jsonPath("$.customerLastName", is(order.getCustomerLastName())))
				.andExpect(jsonPath("$.contactTel", is(order.getContactTel())))
				.andExpect(jsonPath("$.email.email", is(order.getEmail().getEmail())))
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
		logger.info("New order test #4: successfully created order with existing email and existing address");
	}

	@Test
	@DisplayName("New order test #5: create order with invalid contact number")
	public void givenOrderWithInvalidContactNumber_whenCreateOrUpdate_thenThrowException() throws Exception {

		// given / preparation:
		Order order = new Order();
		order.setCustomerFirstName("InvalidContactNumber");
		order.setContactTel(123);
		order.setEmail(new Email("invalidContactTel@email.com"));
		order.setAddress(firstAddress);
		order.setOrderDetails(orderDetails);
		order.setCart(cart);

		// action: send post request
		ResultActions orderResponse = mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert: exception is thrown
		logger.info("New order test #5: create order with invalid contact number");
		orderResponse.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
				CoreMatchers.instanceOf(InvalidContactTelephoneException.class)));
		logger.info("New order test #5: successfully NOT created order with invalid contact number");
	}

	@Test
	@DisplayName("New order test #6: create order with invalid change request")
	public void givenOrderWithInvalidChangeRequest_whenCreateOrUpdate_thenThrowException() throws Exception {

		// given / preparation:
		Order order = new Order();

		order.setCustomerFirstName("InvalidChangeRequest");
		order.setContactTel(123456789);
		order.setEmail(new Email("invalidChangeRequest@email.com"));
		order.setAddress(firstAddress);
		order.setOrderDetails(orderDetails);
		order.getOrderDetails().setPaymentType("Cash");
		order.getOrderDetails().setChangeRequested(10D);
		order.setCart(cart);

		// action: send post request
		ResultActions orderResponse = mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert: exception is thrown
		logger.info("New order test #6: create order with invalid change request");
		orderResponse.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
				CoreMatchers.instanceOf(InvalidChangeRequestedException.class)));
		logger.info("New order test #6: successfully NOT created order with invalid change request ");
	}

	@Test
	@DisplayName("New order test #7: create order with empty cart")
	public void givenOrderWithEmptyCart_whenCreateOrUpdate_thenThrowException() throws Exception {

		// given / preparation:
		Order order = new Order();
		order.setCustomerFirstName("InvalidChangeRequest");
		order.setContactTel(123456789);
		order.setEmail(new Email("invalidChangeRequest@email.com"));
		order.setAddress(firstAddress);
		order.setOrderDetails(orderDetails);

		Cart emptyCart = new Cart();
		emptyCart.setTotalQuantity(0);
		order.setCart(emptyCart);

		// action: send post request
		ResultActions orderResponse = mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert: exception is thrown
		logger.info("New order test #7: create order with empty cart");
		orderResponse.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
				CoreMatchers.instanceOf(EmptyCartException.class)));
		logger.info("New order test #7: successfully NOT created order with empty cart");
	}

	@Test
	@DisplayName("New order test #8: create order with invalid first name, invalid address fields, empty email")
	public void givenOrderInvalidFirstNameAndAddressAndEmptyEmail_whenCreateOrUpdate_thenThrowException()
			throws Exception {

		// given / preparation:
		Order order = new Order();
		order.setCustomerFirstName("asd321%$·-%%");
		order.setContactTel(123456789);
		order.setEmail(new Email(""));
		order.setAddress(new Address("4325vf·!", 15, "·$%", "", "%·", "--sdf$·"));
		order.setOrderDetails(orderDetails);
		order.setCart(cart);

		// action: send post request
		ResultActions orderResponse = mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert: exception is thrown
		logger.info("New order test #8: create order with invalid first name, invalid address fields, empty email");
		orderResponse.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
				CoreMatchers.instanceOf(MethodArgumentNotValidException.class)));
		logger.info(
				"New order test #8: successfully NOT created order with invalid first name, invalid address fields, empty email");
	}
}