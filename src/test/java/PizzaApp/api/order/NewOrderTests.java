package PizzaApp.api.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;
import java.util.logging.Logger;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;

import static org.junit.jupiter.api.Assertions.*;

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
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.fasterxml.jackson.databind.ObjectMapper;
import PizzaApp.api.repos.order.OrderRepository;
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
	private OrderRepository orderRepository;

	@Autowired
	private ObjectMapper objectMapper;

	// test objects
	private final Address firstAddress = new Address.Builder()
			.withStreet("FirstAddress")
			.withStreetNr(15)
			.withFloor("3")
			.withDoor("2A")
			.build();
	private final Address secondAddress = new Address.Builder()
			.withStreet("SecondAddress")
			.withStreetNr(33)
			.withStaircase("DER")
			.withFloor("9")
			.withDoor("E")
			.build();

	private final Email firstEmail = new Email.Builder()
			.withEmail("firstEmail@email.com")
			.build();
	private final Email secondEmail = new Email.Builder()
			.withEmail("secondEmail@email.com")
			.build();

	private final OrderDetails orderDetails = new OrderDetails.Builder()
			.withDeliveryHour("ASAP")
			.withPaymentType("Credit Card")
			.build();
	private final Cart cart = new Cart.Builder()
			.withOrderItems(List.of(new OrderItem.Builder()
					.withProductType("pizza")
					.withWithName("Carbonara")
					.withFormat("Mediana")
					.withQuantity(1)
					.withPrice(14.75)
					.build()))
			.withTotalQuantity(1)
			.withTotalCost(14.75)
			.withTotalCostOffers(0D)
			.build();

	@Test
	@org.junit.jupiter.api.Order(1)
	public void givenOrder_whenCreateOrUpdate_thenReturnOrder() throws Exception {
		logger.info("New order test #1: create order");

		// given / preparation:
		Order order = new Order.Builder()
				.withCustomerFirstName("FirstCustomer")
				.withCustomerLastName("FirstTel")
				.withContactTel(666333999)
				.withEmail(firstEmail)
				.withAddress(firstAddress)
				.withOrderDetails(orderDetails)
				.withCart(cart)
				.build();

		// action: persist and retrieve order
		Order dbOrder = orderRepository.findById(Long.valueOf(
				mockMvc.perform(post("/api/order")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(order)))
						.andReturn().getResponse().getContentAsString()));

		// set createdOn to do equality test
		order.setCreatedOn(dbOrder.getCreatedOn());

		// then expect/assert: returned data matches set data
		assertTrue(dbOrder.entityEquals(order));

		logger.info("New order test #1: successfully created order");
	}

	@Test
	public void givenOrderWithExistingEmail_whenCreateOrUpdate_thenUseExistingEmail() throws Exception {
		logger.info("New order test #2: create order with existing email and new address");

		// given / preparation:
		Order order = new Order.Builder()
				.withCustomerFirstName("FirstCustomer")
				.withCustomerLastName("FirstTel")
				.withContactTel(666333999)
				.withEmail(firstEmail)
				.withAddress(secondAddress)
				.withOrderDetails(orderDetails)
				.withCart(cart)
				.build();

		// action: persist and retrieve order
		Order dbOrder = orderRepository.findById(Long.valueOf(
				mockMvc.perform(post("/api/order")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(order)))
						.andReturn().getResponse().getContentAsString()));

		// set createdOn to do equality test
		order.setCreatedOn(dbOrder.getCreatedOn());

		// then expect/assert: returned data matches set data
		assertTrue(dbOrder.entityEquals(order));

		logger.info("New order test #2: successfully created order with existing email and new address");
	}

	@Test
	public void givenOrderWithExistingAddress_whenCreateOrUpdate_thenUseExistingAddress() throws Exception {
		logger.info("New order test #3: create order with new email and existing address");

		// given / preparation:
		Order order = new Order.Builder()
				.withCustomerFirstName("SecondCustomer")
				.withCustomerLastName("SecondTel")
				.withContactTel(123456789)
				.withEmail(secondEmail)
				.withAddress(firstAddress)
				.withOrderDetails(orderDetails)
				.withCart(cart)
				.build();

		// action: persist and retrieve order
		Order dbOrder = orderRepository.findById(Long.valueOf(
				mockMvc.perform(post("/api/order")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(order)))
						.andReturn().getResponse().getContentAsString()));

		// set createdOn to do equality test
		order.setCreatedOn(dbOrder.getCreatedOn());

		// then expect/assert: returned data matches set data
		assertTrue(dbOrder.entityEquals(order));

		logger.info("New order test #3: successfully created order with new email and existing address");
	}

	@Test
	public void givenOrderWithExistingEmailAndAddress_whenCreateOrUpdate_thenUseExistingEmailAndAddress() throws Exception {
		logger.info("New order test #4: create order with existing email and existing address");

		// given / preparation:
		Order order = new Order.Builder()
				.withCustomerFirstName("FirstCustomerAgain")
				.withCustomerLastName("FirstTelAgain")
				.withContactTel(666333999)
				.withEmail(firstEmail)
				.withAddress(firstAddress)
				.withOrderDetails(orderDetails)
				.withCart(cart)
				.build();

		// action: persist and retrieve order
		Order dbOrder = orderRepository.findById(Long.valueOf(
				mockMvc.perform(post("/api/order")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(order)))
						.andReturn().getResponse().getContentAsString()));

		// set createdOn to do equality test
		order.setCreatedOn(dbOrder.getCreatedOn());

		// then expect/assert: returned data matches set data
		assertTrue(dbOrder.entityEquals(order));

		logger.info("New order test #4: successfully created order with existing email and existing address");
	}

	@Test
	public void givenOrderWithInvalidContactNumber_whenCreateOrUpdate_thenThrowException() throws Exception {
		logger.info("New order test #5: create order with invalid contact number");

		// given / preparation:
		Order order = new Order.Builder()
				.withCustomerFirstName("InvalidContactNumber")
				.withCustomerLastName("WillThrow")
				.withContactTel(123)
				.withEmail(firstEmail)
				.withAddress(firstAddress)
				.withOrderDetails(orderDetails)
				.withCart(cart)
				.build();

		// action: send post request and expect exception is thrown
		mockMvc.perform(post("/api/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(InvalidContactTelephoneException.class)));

		logger.info("New order test #5: successfully NOT created order with invalid contact number");
	}

	@Test
	public void givenOrderWithInvalidChangeRequest_whenCreateOrUpdate_thenThrowException() throws Exception {
		logger.info("New order test #6: create order with invalid change request");

		// given / preparation:
		Order order = new Order.Builder()
				.withCustomerFirstName("InvalidChangeRequest")
				.withCustomerLastName("WillThrow")
				.withContactTel(123456789)
				.withEmail(firstEmail)
				.withAddress(firstAddress)
				.withOrderDetails(orderDetails)
				.withCart(cart)
				.build();
		order.getOrderDetails().setPaymentType("Cash");
		order.getOrderDetails().setChangeRequested(10D);

		// action: send post request and expect ex is thrown
		mockMvc.perform(post("/api/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(InvalidChangeRequestedException.class)));

		logger.info("New order test #6: successfully NOT created order with invalid change request ");
	}

	@Test
	public void givenOrderWithEmptyCart_whenCreateOrUpdate_thenThrowException() throws Exception {
		logger.info("New order test #7: create order with empty cart");

		// given / preparation:
		Order order = new Order.Builder()
				.withCustomerFirstName("EmptyCart")
				.withCustomerLastName("WillThrow")
				.withContactTel(333333333)
				.withEmail(firstEmail)
				.withAddress(firstAddress)
				.withOrderDetails(orderDetails)
				.withCart(new Cart.Builder().withTotalQuantity(0).withEmptyItemList().build())
				.build();

		// action: send post request and expect ex is thrown
		mockMvc.perform(post("/api/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(EmptyCartException.class)));

		logger.info("New order test #7: successfully NOT created order with empty cart");
	}

	@Test
	public void givenOrderInvalidFirstNameAndAddressAndEmptyEmail_whenCreateOrUpdate_thenThrowException() throws Exception {
		logger.info("New order test #8: create order with invalid first name, invalid address fields, empty email");

		// given / preparation:
		Order order = new Order.Builder()
				.withCustomerFirstName("asd321%$·-%%")
				.withContactTel(333333333)
				.withEmail(new Email.Builder().build())
				.withAddress(new Address.Builder()
						.withStreet("4325vf·!")
						.withStreetNr(3)
						.withStaircase("·$%")
						.withFloor("%·")
						.withDoor("--sdf$·").build())
				.withOrderDetails(orderDetails)
				.withCart(cart)
				.build();

		// action: send post request and expect ex is thrown
		mockMvc.perform(post("/api/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(MethodArgumentNotValidException.class)));

		logger.info(
				"New order test #8: successfully NOT created order with invalid first name, invalid address fields, empty email");
	}
}