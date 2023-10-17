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
import PizzaApp.api.entity.order.cart.Cart;
import PizzaApp.api.entity.user.Address;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.OrderItem;
import PizzaApp.api.exceptions.exceptions.order.EmptyCartException;
import PizzaApp.api.exceptions.exceptions.order.InvalidChangeRequestedException;

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

	@Test
	public void givenOrder_whenCreate_thenReturnOrder() throws Exception {
		logger.info("New order test: create order");

		// given / preparation:
		Order order = new Order.Builder()
				.withCustomerName("FirstCustomer")
				.withContactTel(666333999)
				.withEmail("anEmail@email.com")
				.withAddress(new Address.Builder()
						.withStreet("FirstAddress")
						.withStreetNr(15)
						.withFloor("3")
						.withDoor("2A")
						.build())
				.withOrderDetails(new OrderDetails.Builder()
						.withDeliveryHour("ASAP")
						.withPaymentType("Credit Card")
						.build())
				.withCart(new Cart.Builder()
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
						.build())
				.build();

		// action: persist and retrieve order
		Order dbOrder = orderRepository.findById(Long.valueOf(mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andReturn().getResponse().getContentAsString()));

		// set createdOn to do equality test
		order.setCreatedOn(dbOrder.getCreatedOn());

		// then expect/assert: returned data matches set data
		assertTrue(dbOrder.entityEquals(order));

		logger.info("New order test: successfully created order");
	}

	@Test
	public void givenOrderWithExistingAddress_whenCreate_thenUseExistingAddress() throws Exception {
		logger.info("New order test: create order with existing address");

		// given / preparation:
		Order order = new Order.Builder()
				.withCustomerName("SecondCustomer SecondTel")
				.withContactTel(123456789)
				.withEmail("anEmail@email.com")
				.withAddress(new Address.Builder()
						.withStreet("FirstAddress")
						.withStreetNr(15)
						.withFloor("3")
						.withDoor("2A")
						.build())
				.withOrderDetails(new OrderDetails.Builder()
						.withDeliveryHour("ASAP")
						.withPaymentType("Credit Card")
						.build())
				.withCart(new Cart.Builder()
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
						.build())
				.build();

		// action: persist and retrieve order
		Order dbOrder = orderRepository.findById(Long.valueOf(mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andReturn().getResponse().getContentAsString()));

		// set createdOn to do equality test
		order.setCreatedOn(dbOrder.getCreatedOn());

		// then expect/assert: returned data matches set data
		assertTrue(dbOrder.entityEquals(order));

		logger.info("New order test: successfully created order with existing address");
	}

	@Test
	public void givenOrderWithInvalidContactNumber_whenCreate_thenThrowException() throws Exception {
		logger.info("New order test: create order with invalid contact number");

		// given / preparation:
		Order order = new Order.Builder()
				.withCustomerName("WillThrowInvalidContact")
				.withContactTel(123)
				.withEmail("anEmail@email.com")
				.withAddress(new Address.Builder()
						.withStreet("FirstAddress")
						.withStreetNr(15)
						.withFloor("3")
						.withDoor("2A")
						.build())
				.withOrderDetails(new OrderDetails.Builder()
						.withDeliveryHour("ASAP")
						.withPaymentType("Credit Card")
						.build())
				.withCart(new Cart.Builder()
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
						.build())
				.build();

		// action: send post request and expect exception is thrown
		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(MethodArgumentNotValidException.class)));

		logger.info("New order test: successfully NOT created order with invalid contact number");
	}

	@Test
	public void givenOrderInvalidName_whenCreate_thenThrowException() throws Exception {
		logger.info("New order test: create order with invalid name");

		// given / preparation:
		Order order = new Order.Builder()
				.withCustomerName("asd321%$·-%%")
				.withContactTel(333333333)
				.withEmail("anEmail@email.com")
				.withAddress(new Address.Builder()
						.withStreet("FirstAddress")
						.withStreetNr(15)
						.withFloor("3")
						.withDoor("2A")
						.build())
				.withOrderDetails(new OrderDetails.Builder()
						.withDeliveryHour("ASAP")
						.withPaymentType("Credit Card")
						.build())
				.withCart(new Cart.Builder()
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
						.build())
				.build();

		// action: send post request and expect ex is thrown
		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(MethodArgumentNotValidException.class)));

		logger.info(
				"New order test: successfully NOT created order with invalid name");
	}

	@Test
	public void givenOrderInvalidEmail_whenCreate_thenThrowException() throws Exception {
		logger.info("New order test: create order with invalid email");

		// given / preparation:
		Order order = new Order.Builder()
				.withCustomerName("dirtyCustomer")
				.withContactTel(333333333)
				.withEmail("<script>alert('hacker')</script>")
				.withAddress(new Address.Builder()
						.withStreet("FirstAddress")
						.withStreetNr(15)
						.withFloor("3")
						.withDoor("2A")
						.build())
				.withOrderDetails(new OrderDetails.Builder()
						.withDeliveryHour("ASAP")
						.withPaymentType("Credit Card")
						.build())
				.withCart(new Cart.Builder()
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
						.build())
				.build();

		// action: send post request and expect ex is thrown
		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(MethodArgumentNotValidException.class)));

		logger.info(
				"New order test: successfully NOT created order with invalid email");
	}

	@Test
	public void givenOrderInvalidEmailTwo_whenCreate_thenThrowException() throws Exception {
		logger.info("New order test: create order with invalid email second case");

		// given / preparation:
		Order order = new Order.Builder()
				.withCustomerName("dirtyCustomer")
				.withContactTel(333333333)
				.withEmail("DELETE * FROM customer_order;")
				.withAddress(new Address.Builder()
						.withStreet("FirstAddress")
						.withStreetNr(15)
						.withFloor("3")
						.withDoor("2A")
						.build())
				.withOrderDetails(new OrderDetails.Builder()
						.withDeliveryHour("ASAP")
						.withPaymentType("Credit Card")
						.build())
				.withCart(new Cart.Builder()
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
						.build())
				.build();

		// action: send post request and expect ex is thrown
		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(MethodArgumentNotValidException.class)));

		logger.info(
				"New order test: successfully NOT created order with invalid email second case");
	}

	@Test
	public void givenOrderWithInvalidChangeRequest_whenCreate_thenThrowException() throws Exception {
		logger.info("New order test: create order with invalid change request");

		// given / preparation:
		Order order = new Order.Builder()
				.withCustomerName("WillThrowInvalidChangeRequest")
				.withContactTel(123456789)
				.withEmail("anEmail@email.com")
				.withAddress(new Address.Builder()
						.withStreet("FirstAddress")
						.withStreetNr(15)
						.withFloor("3")
						.withDoor("2A")
						.build())
				.withCart(new Cart.Builder()
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
						.build())
				.withOrderDetails(new OrderDetails.Builder()
						.withDeliveryHour("ASAP")
						.withPaymentType("Cash")
						.withChangeRequested(10D)
						.build())
				.build();

		// action: send post request and expect ex is thrown
		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(InvalidChangeRequestedException.class)));

		logger.info("New order test: successfully NOT created order with invalid change request ");
	}

	@Test
	public void givenOrderWithEmptyCart_whenCreate_thenThrowException() throws Exception {
		logger.info("New order test: create order with empty cart");

		// given / preparation:
		Order order = new Order.Builder()
				.withCustomerName("WillThrowEmptyCart")
				.withContactTel(333333333)
				.withEmail("anEmail@email.com")
				.withAddress(new Address.Builder()
						.withStreet("FirstAddress")
						.withStreetNr(15)
						.withFloor("3")
						.withDoor("2A")
						.build())
				.withOrderDetails(new OrderDetails.Builder()
						.withDeliveryHour("ASAP")
						.withPaymentType("Credit Card")
						.build())
				.withCart(new Cart.Builder().withTotalQuantity(0).withEmptyItemList().build())
				.build();

		// action: send post request and expect ex is thrown
		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(EmptyCartException.class)));

		logger.info("New order test: successfully NOT created order with empty cart");
	}
}