package PizzaApp.api.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;
import java.util.logging.Logger;

import PizzaApp.api.entity.order.dto.NewAnonOrderDTO;
import PizzaApp.api.services.order.OrderService;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.OrderItem;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class AnonOrderTests {

	private final Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private OrderService orderService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void givenOrder_whenCreate_thenReturnOrder() throws Exception {
		logger.info("New order test: create order");

		NewAnonOrderDTO newAnonOrderDTO = new NewAnonOrderDTO(
				"CustomerName",
				111222333,
				"customer@gmail.com",
				new Address.Builder()
						.withStreet("FirstAddress")
						.withStreetNr(15)
						.withFloor("3")
						.withDoor("2A")
						.build(),
				new OrderDetails.Builder()
						.withDeliveryHour("ASAP")
						.withPaymentType("Credit Card")
						.build(),
				new Cart.Builder()
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
						.build()
		);

		// action: persist and retrieve order
		Order dbOrder = orderService.findByIdNoLazy(Long.valueOf(mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newAnonOrderDTO)))
				.andReturn().getResponse().getContentAsString()));


		// then expect/assert: returned data matches set data
		assertAll("Data returned matches expected values",
				() -> assertEquals(dbOrder.getAnonCustomerName(), newAnonOrderDTO.anonCustomerName()),
				() -> assertEquals(dbOrder.getAnonCustomerContactNumber(), newAnonOrderDTO.anonCustomerContactNumber()),
				() -> assertEquals(dbOrder.getAnonCustomerEmail(), newAnonOrderDTO.anonCustomerEmail()),
				() -> assertTrue(dbOrder.getAddress().contentEquals(newAnonOrderDTO.address())),
				() -> assertTrue(dbOrder.getOrderDetails().contentEquals(newAnonOrderDTO.orderDetails())),
				() -> assertTrue(dbOrder.getCart().contentEquals(newAnonOrderDTO.cart())
				));

		logger.info("New order test: successfully created order");
	}

	@Test
	public void givenOrderWithInvalidContactNumber_whenCreate_thenThrowException() throws Exception {
		logger.info("New order test: create order with invalid contact number");

		NewAnonOrderDTO newAnonOrderDTO = new NewAnonOrderDTO(
				"CustomerName",
				123,
				"customer@gmail.com",
				new Address.Builder()
						.withStreet("FirstAddress")
						.withStreetNr(15)
						.withFloor("3")
						.withDoor("2A")
						.build(),
				new OrderDetails.Builder()
						.withDeliveryHour("ASAP")
						.withPaymentType("Credit Card")
						.build(),
				new Cart.Builder()
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
						.build()
		);

		// action: send post request and expect exception is thrown
		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newAnonOrderDTO)))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(MethodArgumentNotValidException.class)));

		logger.info("New order test: successfully NOT created order with invalid contact number");
	}

	@Test
	public void givenOrderInvalidName_whenCreate_thenThrowException() throws Exception {
		logger.info("New order test: create order with invalid name");

		NewAnonOrderDTO newAnonOrderDTO = new NewAnonOrderDTO(
				"asd321%$·-%%",
				111222333,
				"customer@gmail.com",
				new Address.Builder()
						.withStreet("FirstAddress")
						.withStreetNr(15)
						.withFloor("3")
						.withDoor("2A")
						.build(),
				new OrderDetails.Builder()
						.withDeliveryHour("ASAP")
						.withPaymentType("Credit Card")
						.build(),
				new Cart.Builder()
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
						.build()
		);

		// action: send post request and expect ex is thrown
		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newAnonOrderDTO)))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(MethodArgumentNotValidException.class)));

		logger.info("New order test: successfully NOT created order with invalid name");
	}

	@Test
	public void givenOrderInvalidEmail_whenCreate_thenThrowException() throws Exception {
		logger.info("New order test: create order with invalid email");

		NewAnonOrderDTO newAnonOrderDTO = new NewAnonOrderDTO(
				"CustomerName",
				111222333,
				"<script>alert('hacker')</script>",
				new Address.Builder()
						.withStreet("FirstAddress")
						.withStreetNr(15)
						.withFloor("3")
						.withDoor("2A")
						.build(),
				new OrderDetails.Builder()
						.withDeliveryHour("ASAP")
						.withPaymentType("Credit Card")
						.build(),
				new Cart.Builder()
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
						.build()
		);

		// action: send post request and expect ex is thrown
		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newAnonOrderDTO)))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(MethodArgumentNotValidException.class)));

		logger.info("New order test: successfully NOT created order with invalid email");
	}

	@Test
	public void givenOrderInvalidEmailTwo_whenCreate_thenThrowException() throws Exception {
		logger.info("New order test: create order with invalid email second case");

		NewAnonOrderDTO newAnonOrderDTO = new NewAnonOrderDTO(
				"CustomerName",
				111222333,
				"DELETE * FROM orders;",
				new Address.Builder()
						.withStreet("FirstAddress")
						.withStreetNr(15)
						.withFloor("3")
						.withDoor("2A")
						.build(),
				new OrderDetails.Builder()
						.withDeliveryHour("ASAP")
						.withPaymentType("Credit Card")
						.build(),
				new Cart.Builder()
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
						.build()
		);

		// action: send post request and expect ex is thrown
		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newAnonOrderDTO)))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(MethodArgumentNotValidException.class)));

		logger.info("New order test: successfully NOT created order with invalid email second case");
	}

	@Test
	public void givenOrderWithInvalidChangeRequest_whenCreate_thenThrowException() throws Exception {
		logger.info("New order test: create order with invalid change request");

		NewAnonOrderDTO newAnonOrderDTO = new NewAnonOrderDTO(
				"CustomerName",
				111222333,
				"customer@gmail.com",
				new Address.Builder()
						.withStreet("FirstAddress")
						.withStreetNr(15)
						.withFloor("3")
						.withDoor("2A")
						.build(),
				new OrderDetails.Builder()
						.withDeliveryHour("ASAP")
						.withPaymentType("Cash")
						.withChangeRequested(10D)
						.build(),
				new Cart.Builder()
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
						.build()
		);

		// action: send post request and expect ex is thrown
		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newAnonOrderDTO)))
				.andExpect(status().isBadRequest());

		logger.info("New order test: successfully NOT created order with invalid change request ");
	}

	@Test
	public void givenOrderWithEmptyCart_whenCreate_thenThrowException() throws Exception {
		logger.info("New order test: create order with empty cart");

		NewAnonOrderDTO newAnonOrderDTO = new NewAnonOrderDTO(
				"CustomerName",
				111222333,
				"customer@gmail.com",
				new Address.Builder()
						.withStreet("FirstAddress")
						.withStreetNr(15)
						.withFloor("3")
						.withDoor("2A")
						.build(),
				new OrderDetails.Builder()
						.withDeliveryHour("ASAP")
						.withPaymentType("Cash")
						.build(),
				new Cart.Builder().withTotalQuantity(0).withEmptyItemList().build()
		);

		// action: send post request and expect ex is thrown
		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newAnonOrderDTO)))
				.andExpect(status().isBadRequest());

		logger.info("New order test: successfully NOT created order with empty cart");
	}
}