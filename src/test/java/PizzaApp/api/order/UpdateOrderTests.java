package PizzaApp.api.order;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import PizzaApp.api.services.order.OrderService;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import PizzaApp.api.entity.order.cart.Cart;
import PizzaApp.api.entity.user.Address;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.OrderItem;
import PizzaApp.api.exceptions.exceptions.order.OrderDataUpdateTimeLimitException;
import PizzaApp.api.exceptions.exceptions.order.OrderDeleteTimeLimitException;

// TODO - update this test class as now only logged in user can update an order

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class UpdateOrderTests {

	private final Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OrderService ordersService;

	// test objects
	private final Address originalAddress = new Address.Builder()
			.withStreet("OriginalAddress")
			.withStreetNr(12)
			.withStaircase("C")
			.withFloor("11")
			.withDoor("DER")
			.build();

	private final String originalEmail = "originalEmail@email.com";

	private final OrderDetails originalOrderDetails = new OrderDetails.Builder()
			.withDeliveryHour("ASAP")
			.withPaymentType("Credit Card")
			.build();

	private final Cart originalCart = new Cart.Builder()
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

	private LocalDateTime originalOrderCreatedOn;
	private Long originalOrderId;

	@Test
	@org.junit.jupiter.api.Order(1)
	public void givenOrder_whenCreateOrUpdate_thenReturnOrder() throws Exception {
		logger.info("Update order test #1: correctly created original order");

		Order order = new Order.Builder()
				.withCustomerName("OriginalCustomer")
				.withContactTel(333666999)
				.withEmail(originalEmail)
				.withAddress(originalAddress)
				.withOrderDetails(originalOrderDetails)
				.withCart(originalCart)
				.build();

		// action: persist and retrieve order
		Order dbOrder = ordersService.findById(mockMvc.perform(post("/api/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andReturn().getResponse().getContentAsString());

		// *** set goodies that will be useful later
		originalOrderId = dbOrder.getId();
		originalOrderCreatedOn = dbOrder.getCreatedOn();
		// ***

		// set the createdOn to do equality test
		order.setCreatedOn(originalOrderCreatedOn);

		// then expect/assert: returned data matches set data
		assertTrue(dbOrder.entityEquals(order));

		logger.info("Update order test #1: successfully created original order");
	}

	@Test
	@org.junit.jupiter.api.Order(2)
	public void givenNewCustomerData_whenCreateOrUpdate_thenReturnOrderWithNewCustomerData() throws Exception {
		logger.info("Update order test #2: update customer data");

		// given / preparation:
		Order order = new Order.Builder()
				.withCustomerName("NewCustomer")
				.withContactTel(123456789)
				.withEmail("newEmail@gmail.com")
				.withId(originalOrderId)
				.withCreatedOn(originalOrderCreatedOn)
				.build();

		// action: persist and retrieve order
		Order dbOrder = ordersService.findById(mockMvc.perform(post("/api/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andReturn().getResponse().getContentAsString());

		// then expect/assert: returned data matches set data
		assertAll("Data returned should match data set for update",
				() -> assertEquals(dbOrder.getCustomerName(), order.getCustomerName()),
				() -> assertEquals(dbOrder.getContactTel(), order.getContactTel()),
				() -> assertEquals(dbOrder.getEmail(), order.getEmail()),
				() -> assertEquals(dbOrder.getId(), order.getId()),
				() -> assertEquals(dbOrder.getCreatedOn(), order.getCreatedOn())
		);
		logger.info("Update order test #2: successfully updated customer data");
	}

	@Test
	@org.junit.jupiter.api.Order(3)
	public void givenNewEmail_whenCreateOrUpdate_thenReturnOrderWithNewEmail() throws Exception {
		logger.info("Update order test #3: update email");

		// given / preparation:
		Order order = new Order.Builder()
				.withCustomerName("NewCustomer")
				.withContactTel(123456789)
				.withEmail("NewEmail@email.com")
				.withId(originalOrderId)
				.withCreatedOn(originalOrderCreatedOn)
				.build();

		// action: persist and retrieve order
		Order dbOrder = ordersService.findById(mockMvc.perform(post("/api/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andReturn().getResponse().getContentAsString());

		// then expect/assert: returned data matches set data
		assertAll("Data returned should match data set for update",
				() -> assertEquals(dbOrder.getCustomerName(), order.getCustomerName()),
				() -> assertEquals(dbOrder.getContactTel(), order.getContactTel()),
				() -> assertEquals(dbOrder.getEmail(), order.getEmail()),
				() -> assertEquals(dbOrder.getId(), order.getId()),
				() -> assertEquals(dbOrder.getCreatedOn(), order.getCreatedOn())
		);
		logger.info("Update order test #3: successfully updated email");
	}

	@Test
	@org.junit.jupiter.api.Order(4)
	public void givenNewAddress_whenCreateOrUpdate_thenReturnOrderWithUpdatedAddress() throws Exception {
		logger.info("Update order test #4: update address");

		// given / preparation:
		Order order = new Order.Builder()
				.withAddress(new Address.Builder()
						.withStreet("NewAddress")
						.withStreetNr(157)
						.build())
				.withId(originalOrderId)
				.withCreatedOn(originalOrderCreatedOn)
				.build();

		// action: persist and retrieve order
		Order dbOrder = ordersService.findById(mockMvc.perform(post("/api/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andReturn().getResponse().getContentAsString());

		// then expect/assert: returned data matches set data
		assertAll("Data returned should match data set for update",
				() -> assertEquals(dbOrder.getId(), order.getId()),
				() -> assertEquals(dbOrder.getCreatedOn(), order.getCreatedOn()),
				() -> assertTrue(dbOrder.getAddress().entityEquals(order.getAddress()))
		);
		logger.info("Update order test #4: successfully updated address");
	}

	@Test
	@org.junit.jupiter.api.Order(5)
	public void givenOrderDetails_whenCreateOrUpdate_thenReturnOrderWithNewOrderDetails() throws Exception {
		logger.info("Update order test #5: update orderDetails");

		// given / preparation:
		Order order = new Order.Builder()
				.withOrderDetails(new OrderDetails.Builder()
						.withDeliveryHour("15:30")
						.withPaymentType("Cash")
						.withChangeRequested(15D)
						.withDeliveryComment("Well cut and hot")
						.build())
				.withId(originalOrderId)
				.withCreatedOn(originalOrderCreatedOn)
				.build();

		// action: persist and retrieve order
		Order dbOrder = ordersService.findById(mockMvc.perform(post("/api/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andReturn().getResponse().getContentAsString());

		// then expect/assert: returned data matches set data
		assertAll("Data returned should match data set for update",
				() -> assertEquals(dbOrder.getId(), order.getId()),
				() -> assertEquals(dbOrder.getCreatedOn(), order.getCreatedOn()),
				() -> assertTrue(dbOrder.getOrderDetails().entityEquals(order.getOrderDetails()))
		);
		logger.info("Update order test #5: successfully updated orderDetails");
	}

	@Test
	@org.junit.jupiter.api.Order(6)
	public void givenCart_whenCreateOrUpdate_thenReturnOrderWithNewCart() throws Exception {
		logger.info("Update order test #6: update cart");

		// given / preparation:
		Order order = new Order.Builder()
				.withCart(new Cart.Builder()
						.withOrderItems(List.of(new OrderItem.Builder()
								.withProductType("pizza")
								.withWithName("Trufa Gourmet")
								.withFormat("Mediana")
								.withQuantity(1)
								.withPrice(14.75)
								.build(), new OrderItem.Builder()
								.withProductType("pizza")
								.withWithName("Carbonara")
								.withFormat("Familiar")
								.withQuantity(1)
								.withPrice(20.25)
								.build()))
						.withTotalQuantity(2)
						.withTotalCost(35D)
						.withTotalCostOffers(27.6)
						.build()
				)
				.withOrderDetails(new OrderDetails.Builder()
						.withDeliveryHour("15.30")
						.withPaymentType("Cash")
						.withChangeRequested(50D)
						.withDeliveryComment("Well cut and hot")
						.build())
				.withId(originalOrderId)
				.withCreatedOn(originalOrderCreatedOn)
				.build();

		// action: persist and retrieve order
		Order dbOrder = ordersService.findById(mockMvc.perform(post("/api/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andReturn().getResponse().getContentAsString());

		// then expect/assert: returned data matches set data
		assertAll("Data returned should match data set for update",
				() -> assertEquals(dbOrder.getId(), order.getId()),
				() -> assertEquals(dbOrder.getCreatedOn(), order.getCreatedOn()),
				() -> assertTrue(dbOrder.getCart().entityEquals(order.getCart())),
				() -> assertTrue(dbOrder.getOrderDetails().entityEquals(order.getOrderDetails()))
		);
		logger.info("Update order test #6: successfully updated cart");
	}

	@Test
	@org.junit.jupiter.api.Order(7)
	public void givenCartUpdateAfterTimeLimit_whenCreateOrUpdate_thenCorrectlySetNewCartToNullAndUseOriginalCart() throws Exception {
		logger.info("Update order test #7: update cart after time limit");
		// given / preparation:

		// create order which will be used to perform test
		Long orderId = createTestSubject(16);

		// create order for update
		Order order = new Order.Builder()
				.withCart(new Cart.Builder()
						.withOrderItems(List.of(new OrderItem.Builder()
								.withProductType("pizza")
								.withWithName("Roni Pepperoni")
								.withFormat("Familiar")
								.withQuantity(1)
								.withPrice(18.30)
								.build()))
						.withTotalQuantity(1)
						.withTotalCost(18.30)
						.withTotalCostOffers(0D)
						.build())
				.withOrderDetails(new OrderDetails.Builder()
						.withDeliveryHour("ASAP")
						.withPaymentType("Card")
						.withDeliveryComment("Without oregano")
						.build())
				.withId(orderId)
				.build();

		// action: persist and retrieve order
		Order dbOrder = ordersService.findById(mockMvc.perform(post("/api/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andReturn().getResponse().getContentAsString());

		// set created on to perform equality test
		order.setCreatedOn(dbOrder.getCreatedOn());

		// If the test is successful, the returned order obj will have
		// orderTestSubject's cart (originalCart) because the cart update didn't go through
		// as it was over the time limit. On the other hand,
		// it will have order's orderDetails, since this update did go through
		// because it's before the 20-min time limit for updating orderData.
		assertAll("Data returned should match data set for update",
				() -> assertEquals(dbOrder.getId(), order.getId()),
				() -> assertEquals(dbOrder.getCreatedOn(), order.getCreatedOn()),
				() -> assertTrue(dbOrder.getCart().entityEquals(originalCart)),
				() -> assertTrue(dbOrder.getOrderDetails().entityEquals(order.getOrderDetails()))
		);
		logger.info("Update order test #7: cart successfully NOT updated due to time limit constraint");
	}

	@Test
	@org.junit.jupiter.api.Order(8)
	public void givenOrderDataUpdateAfterTimeLimit_whenCreateOrUpdate_thenThrowException() throws Exception {
		logger.info("Update order test #8: update order data after time limit");
		// order data = customer data (fName, lName, tel, email), address, orderDetails

		// given / preparation:
		// create order which will be used to perform test
		Long orderId = createTestSubject(21);

		// create order for update
		Order order = new Order.Builder()
				.withCustomerName("ExceptionToCatch")
				.withContactTel(123123123)
				.withEmail("wontwork@email.com")
				.withOrderDetails(new OrderDetails.Builder()
						.withDeliveryHour("22:30")
						.withPaymentType("Tarjeta")
						.withDeliveryHour("No llamar al timbre duerme el bebe")
						.build())
				.withId(orderId)
				.build();

		// action: update order and then expect exception is thrown
		mockMvc.perform(put("/api/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(OrderDataUpdateTimeLimitException.class)));

		logger.info("Update order test #8: order data successfully NOT updated due to time limit constraint");
	}

	@Test
	@org.junit.jupiter.api.Order(9)
	public void givenOrderDeleteAfterTimeLimit_whenCreateOrUpdate_thenThrowException() throws Exception {
		logger.info("Update order test #9: delete order after time limit");

		// given / preparation:
		// create order which will be used to perform test
		Long orderId = createTestSubject(36);

		// action: update order
		mockMvc.perform(delete("/api/order/{id}", orderId))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(OrderDeleteTimeLimitException.class)));

		logger.info("Update order test #9: order successfully NOT deleted due to time limit constraint");
	}

	public Long createTestSubject(int minusMins) {
		Order orderTestSubject = new Order.Builder()
				.withCustomerName("orderTestSubject")
				.withContactTel(777333777)
				.withEmail(originalEmail)
				.withAddress(originalAddress)
				.withOrderDetails(originalOrderDetails)
				.withCart(originalCart)
				.withCreatedOn(LocalDateTime.now().minusMinutes(minusMins))
				.build();
		return ordersService.createAnonOrder(orderTestSubject);
	}
}