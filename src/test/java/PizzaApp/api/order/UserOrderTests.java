package PizzaApp.api.order;

import PizzaApp.api.configs.security.utils.SecurityCookieUtils;
import PizzaApp.api.configs.security.utils.SecurityTokenUtils;
import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.dto.error.ApiErrorDTO;
import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.OrderItem;
import PizzaApp.api.entity.order.dto.NewUserOrderDTO;
import PizzaApp.api.entity.order.dto.OrderDTO;
import PizzaApp.api.entity.order.dto.UpdateUserOrderDTO;
import PizzaApp.api.entity.user.dto.PasswordDTO;
import PizzaApp.api.services.address.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class UserOrderTests {

	private final Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SecurityTokenUtils securityTokenUtils;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AddressService addressService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// test objects
	private final OrderDetails testSubjectOrderDetails = new OrderDetails.Builder()
			.withDeliveryHour("ASAP")
			.withPaymentType("Efectivo")
			.withDeliveryComment("Without oregano")
			.build();

	private final Cart testSubjectCart = new Cart.Builder()
			.withTotalQuantity(1)
			.withTotalCost(18.30)
			.withTotalCostOffers(0D)
			.withOrderItems(List.of(new OrderItem.Builder()
					.withProductType("pizza")
					.withWithName("Roni Pepperoni")
					.withFormat("Familiar")
					.withQuantity(1)
					.withPrice(18.30)
					.build()))
			.build();

	private Long addressId, newAddressId;

	@BeforeAll
	public void setup() {
		// create address test subject for testing
		addressId = addressService.create(new Address.Builder()
				.withStreet("Elm street")
				.withStreetNr(15)
				.build());

		newAddressId = addressService.create(new Address.Builder()
				.withStreet("Baker Street")
				.withStreetNr(221)
				.withDoor("b")
				.build());
	}

	@BeforeAll
	@AfterAll
	void cleanUp() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, "users_roles", "users_addresses", "user", "order_item", "cart", "order_details", "orders");
	}

	public Long createUserTestSubject(RegisterDTO registerDTO) throws Exception {
		return Long.valueOf(mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerDTO))
						.with(csrf()))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
	}

	public OrderDTO findOrder(Long orderId, long userId, String validAccessToken) throws Exception {
		String response = mockMvc.perform(get("/api/user/orders/{orderId}", orderId)
				.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
				.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 1800, false, false))
				.with(csrf())).andReturn().getResponse().getContentAsString();
		return objectMapper.readValue(response, OrderDTO.class);
	}

	public OrderDTO createOrderTestSubject(int minutesInThePast, long userId, long addressId, OrderDetails orderDetails, Cart cart, String validAccessToken) throws Exception {
		NewUserOrderDTO order = new NewUserOrderDTO(userId, addressId, orderDetails, cart);
		return findOrder(Long.valueOf(mockMvc.perform(post("/api/user/orders/tests?minusMin={minutesInThePast}", minutesInThePast)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order))
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 1800, false, false))
						.with(csrf()))
				.andReturn().getResponse().getContentAsString()), userId, validAccessToken);
	}

	@Test
	public void givenUserOrder_whenCreate_thenReturnOrder() throws Exception {
		logger.info("Update order test: create user order");

		// create user test subject

		long testUserId = createUserTestSubject(new RegisterDTO(
				"OrderTestsUser",
				"OrderTestCreateOrder@gmail.com",
				"OrderTestCreateOrder@gmail.com",
				"password",
				"password"));

		// create JWT token

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"OrderTestCreateOrder@gmail.com",
				testUserId,
				"USER");

		// create order test subject

		NewUserOrderDTO newOrder = new NewUserOrderDTO(testUserId, addressId, testSubjectOrderDetails, testSubjectCart);

		OrderDTO dbOrder = findOrder(Long.valueOf(mockMvc.perform(post("/api/user/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newOrder))
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(testUserId), 30, false, false))
						.with(csrf()))
				.andReturn().getResponse().getContentAsString()), testUserId, validAccessToken);

		// then expect/assert: returned data matches set data
		assertAll("Data returned matches expected values",
				() -> assertEquals(dbOrder.getUser().id(), newOrder.userId()),
				() -> assertEquals(dbOrder.getAddress().getId(), newOrder.addressId()),
				() -> assertTrue(dbOrder.getCart().contentEquals(newOrder.cart())),
				() -> assertTrue(dbOrder.getOrderDetails().contentEquals(newOrder.orderDetails()))
		);

		logger.info("Update order test: successfully created user order");
	}

	@Test
	public void givenNewAddress_whenUpdate_thenReturnOrderWithUpdatedAddress() throws Exception {
		logger.info("Update order test: update address");

		// create user test subject

		long testUserId = createUserTestSubject(new RegisterDTO(
				"OrderTestsUser",
				"OrderTestUpdateAddress@gmail.com",
				"OrderTestUpdateAddress@gmail.com",
				"password",
				"password"));

		// create JWT token

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"OrderTestUpdateAddress@gmail.com",
				testUserId,
				"USER");

		// create order test subject

		OrderDTO newOrder = createOrderTestSubject(0, testUserId, addressId, testSubjectOrderDetails, testSubjectCart, validAccessToken);

		// update order test subject

		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				newOrder.getId(),
				testUserId,
				newAddressId,
				newOrder.getCreatedOn(),
				newOrder.getOrderDetails(),
				newOrder.getCart());

		OrderDTO updatedOrder = findOrder(Long.valueOf(mockMvc.perform(put("/api/user/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(testUserId), 30, false, false))
						.with(csrf()))
				.andReturn().getResponse().getContentAsString()), testUserId, validAccessToken);

		assertAll("Data returned matches expected values",
				() -> assertEquals(updatedOrder.getId(), orderUpdate.getOrderId()),
				() -> assertEquals(updatedOrder.getAddress().getId(), orderUpdate.getAddressId())
		);

		logger.info("Update order test: successfully updated address");
	}

	@Test
	public void givenNewOrderDetails_whenUpdate_thenReturnOrderWithNewOrderDetails() throws Exception {
		logger.info("Update order test: update orderDetails");

		// create user test subject

		long testUserId = createUserTestSubject(new RegisterDTO(
				"OrderTestsUser",
				"OrderTestUpdateOrderDetails@gmail.com",
				"OrderTestUpdateOrderDetails@gmail.com",
				"password",
				"password"));

		// create JWT token

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"OrderTestUpdateOrderDetails@gmail.com",
				testUserId,
				"USER");

		// create order test subject

		OrderDTO newOrder = createOrderTestSubject(0, testUserId, addressId, testSubjectOrderDetails, testSubjectCart, validAccessToken);

		// update order test subject

		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				newOrder.getId(),
				testUserId,
				newOrder.getAddress().getId(),
				newOrder.getCreatedOn(),
				new OrderDetails.Builder()
						.withId(newOrder.getId())
						.withPaymentType("Efectivo")
						.withDeliveryHour("15:30")
						.withDeliveryComment("Well cut and hot")
						.build(),
				newOrder.getCart());

		OrderDTO updatedOrder = findOrder(Long.valueOf(mockMvc.perform(put("/api/user/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(testUserId), 1800, false, false))
						.with(csrf()))
				.andReturn().getResponse().getContentAsString()), testUserId, validAccessToken);

		assertAll("Data returned matches expected values",
				() -> assertEquals(updatedOrder.getId(), orderUpdate.getOrderId()),
				() -> assertTrue(updatedOrder.getOrderDetails().contentEquals(orderUpdate.getOrderDetails()))
		);

		logger.info("Update order test: successfully updated orderDetails");
	}

	@Test
	public void givenNewCart_whenUpdate_thenReturnOrderWithNewCart() throws Exception {
		logger.info("Update order test: update cart");

		// create user test subject

		long testUserId = createUserTestSubject(new RegisterDTO(
				"OrderTestsUser",
				"OrderTestUpdateOrderCart@gmail.com",
				"OrderTestUpdateOrderCart@gmail.com",
				"password",
				"password"));

		// create JWT token

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"OrderTestUpdateOrderCart@gmail.com",
				testUserId,
				"USER");

		// create order test subject

		OrderDTO newOrder = createOrderTestSubject(0, testUserId, addressId, testSubjectOrderDetails, testSubjectCart, validAccessToken);

		// update order test subject

		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				newOrder.getId(),
				testUserId,
				newOrder.getAddress().getId(),
				newOrder.getCreatedOn(),
				newOrder.getOrderDetails(),
				new Cart.Builder()
						.withId(newOrder.getId())
						.withTotalQuantity(2)
						.withTotalCost(35D)
						.withTotalCostOffers(27.6)
						.withOrderItems(List.of(
								new OrderItem.Builder()
										.withProductType("pizza")
										.withWithName("Trufa Gourmet")
										.withFormat("Mediana")
										.withQuantity(1)
										.withPrice(14.75)
										.build(),
								new OrderItem.Builder()
										.withProductType("pizza")
										.withWithName("Carbonara")
										.withFormat("Familiar")
										.withQuantity(1)
										.withPrice(20.25)
										.build()))
						.build());

		OrderDTO updatedOrder = findOrder(Long.valueOf(mockMvc.perform(put("/api/user/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(testUserId), 1800, false, false))
						.with(csrf()))
				.andReturn().getResponse().getContentAsString()), testUserId, validAccessToken);

		assertAll("Data returned should match data set for update",
				() -> assertEquals(updatedOrder.getId(), orderUpdate.getOrderId()),
				() -> assertTrue(updatedOrder.getCart().contentEquals(orderUpdate.getCart()))
		);

		logger.info("Update order test: successfully updated cart");
	}

	@Test
	public void givenNewCartAndOrderDetails_whenUpdate_thenReturnOrderWithNewCartAndOrderDetails() throws Exception {
		logger.info("Update order test: update cart and orderDetails");

		// create user test subject

		long testUserId = createUserTestSubject(new RegisterDTO(
				"OrderTestsUser",
				"OrderTestUpdateOrderCartAndOrderDetails@gmail.com",
				"OrderTestUpdateOrderCartAndOrderDetails@gmail.com",
				"password",
				"password"));

		// create JWT token

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"OrderTestUpdateOrderCartAndOrderDetails@gmail.com",
				testUserId,
				"USER");

		// create order test subject

		OrderDTO newOrder = createOrderTestSubject(0, testUserId, addressId, testSubjectOrderDetails, testSubjectCart, validAccessToken);

		// update order test subject

		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				newOrder.getId(),
				testUserId,
				newOrder.getAddress().getId(),
				newOrder.getCreatedOn(),
				new OrderDetails.Builder()
						.withId(newOrder.getId())
						.withPaymentType("Efectivo")
						.withDeliveryHour("15:30")
						.withDeliveryComment("Well cut and hot")
						.withChangeRequested(50D)
						.build(),
				new Cart.Builder()
						.withId(newOrder.getId())
						.withTotalQuantity(2)
						.withTotalCost(40.5)
						.withTotalCostOffers(30.37)
						.withOrderItems(List.of(
								new OrderItem.Builder()
										.withProductType("pizza")
										.withWithName("Trufa Gourmet")
										.withFormat("Familiar")
										.withQuantity(1)
										.withPrice(20.25)
										.build(),
								new OrderItem.Builder()
										.withProductType("pizza")
										.withWithName("Carbonara")
										.withFormat("Familiar")
										.withQuantity(1)
										.withPrice(20.25)
										.build()))
						.build());

		OrderDTO updatedOrder = findOrder(Long.valueOf(mockMvc.perform(put("/api/user/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(testUserId), 1800, false, false))
						.with(csrf()))
				.andReturn().getResponse().getContentAsString()), testUserId, validAccessToken);

		assertAll("Data returned should match data set for update",
				() -> assertEquals(updatedOrder.getId(), orderUpdate.getOrderId()),
				() -> assertTrue(updatedOrder.getCart().contentEquals(orderUpdate.getCart())),
				() -> assertTrue(updatedOrder.getOrderDetails().contentEquals(orderUpdate.getOrderDetails()))
		);

		logger.info("Update order test: successfully updated cart and orderDetails");
	}

	// testing ValidateOrderOperation aspect
	@Test
	public void givenCartUpdateAfterTimeLimit_whenUpdate_thenCorrectlySetNewCartToNullAndUseOriginalCart() throws Exception {
		logger.info("Update order test: update cart after time limit");

		// create user test subject

		long testUserId = createUserTestSubject(new RegisterDTO(
				"OrderTestsUser",
				"OrderTestUpdateOrderCartAfterTimeLimit@gmail.com",
				"OrderTestUpdateOrderCartAfterTimeLimit@gmail.com",
				"password",
				"password"));

		// create JWT token

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"OrderTestUpdateOrderCartAfterTimeLimit@gmail.com",
				testUserId,
				"USER");

		// create order test subject

		OrderDTO newOrder = createOrderTestSubject(11, testUserId, addressId, testSubjectOrderDetails, testSubjectCart, validAccessToken);

		// update order test subject

		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				newOrder.getId(),
				testUserId,
				newOrder.getAddress().getId(),
				newOrder.getCreatedOn(),
				newOrder.getOrderDetails(),
				new Cart.Builder()
						.withId(newOrder.getId())
						.withTotalQuantity(2)
						.withTotalCost(35D)
						.withTotalCostOffers(27.6)
						.withOrderItems(List.of(
								new OrderItem.Builder()
										.withProductType("pizza")
										.withWithName("Trufa Gourmet")
										.withFormat("Mediana")
										.withQuantity(1)
										.withPrice(14.75)
										.build(),
								new OrderItem.Builder()
										.withProductType("pizza")
										.withWithName("Carbonara")
										.withFormat("Familiar")
										.withQuantity(1)
										.withPrice(20.25)
										.build()))
						.build());

		OrderDTO updatedOrder = findOrder(Long.valueOf(mockMvc.perform(put("/api/user/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(testUserId), 1800, false, false))
						.with(csrf()))
				.andReturn().getResponse().getContentAsString()), testUserId, validAccessToken);

		assertAll("Data returned matches expected values",
				() -> assertEquals(updatedOrder.getId(), newOrder.getId()),
				() -> assertTrue(updatedOrder.getCart().contentEquals(newOrder.getCart())),
				() -> assertTrue(updatedOrder.getOrderDetails().contentEquals(newOrder.getOrderDetails()))
		);

		logger.info("Update order test: cart successfully NOT updated due to time limit constraint");
	}

	// testing ValidateOrderOperation aspect
	@Test
	public void givenOrderDataUpdateAfterTimeLimit_whenUpdate_thenDontAllowUpdate() throws Exception {
		logger.info("Update order test: update order data after time limit");

		// create user test subject

		long testUserId = createUserTestSubject(new RegisterDTO(
				"OrderTestsUser",
				"OrderTestUpdateOrderDataAfterTimeLimit@gmail.com",
				"OrderTestUpdateOrderDataAfterTimeLimit@gmail.com",
				"password",
				"password"));

		// create JWT token

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"OrderTestUpdateOrderDataAfterTimeLimit@gmail.com",
				testUserId,
				"USER");

		// create order test subject

		OrderDTO newOrder = createOrderTestSubject(16, testUserId, addressId, testSubjectOrderDetails, testSubjectCart, validAccessToken);

		// update order test subject

		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				newOrder.getId(),
				testUserId,
				newOrder.getAddress().getId(),
				newOrder.getCreatedOn(),
				new OrderDetails.Builder()
						.withId(newOrder.getId())
						.withPaymentType("Efectivo")
						.withChangeRequested(15D)
						.withDeliveryHour("15:30")
						.withDeliveryComment("Well cut and hot")
						.build(),
				new Cart.Builder()
						.withId(newOrder.getId())
						.withTotalQuantity(2)
						.withTotalCost(35D)
						.withTotalCostOffers(27.6)
						.withOrderItems(List.of(
								new OrderItem.Builder()
										.withProductType("pizza")
										.withWithName("Trufa Gourmet")
										.withFormat("Mediana")
										.withQuantity(1)
										.withPrice(14.75)
										.build(),
								new OrderItem.Builder()
										.withProductType("pizza")
										.withWithName("Carbonara")
										.withFormat("Familiar")
										.withQuantity(1)
										.withPrice(20.25)
										.build()))
						.build());

		String response = mockMvc.perform(put("/api/user/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(testUserId), 1800, false, false))
						.with(csrf()))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

		ApiErrorDTO error = objectMapper.readValue(response, ApiErrorDTO.class);

		assertEquals("El tiempo límite para actualizar el pedido (15 minutos) ha finalizado.", error.errorMsg());

		logger.info("Update order test: order data successfully NOT updated due to time limit constraint");
	}

	// testing ValidateOrderOperation aspect
	@Test
	public void givenOrderDeleteAfterTimeLimit_whenDelete_thenDontAllowDelete() throws Exception {
		logger.info("Update order test: delete order after time limit");

		// create user test subject

		long testUserId = createUserTestSubject(new RegisterDTO(
				"OrderTestsUser",
				"OrderTestDeleteOrderAfterTimeLimite@gmail.com",
				"OrderTestDeleteOrderAfterTimeLimite@gmail.com",
				"password",
				"password"));

		// create JWT token

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"OrderTestDeleteOrderAfterTimeLimite@gmail.com",
				testUserId,
				"USER");

		// create order test subject

		OrderDTO newOrder = createOrderTestSubject(21, testUserId, addressId, testSubjectOrderDetails, testSubjectCart, validAccessToken);

		// delete order test subject

		String response = mockMvc.perform(delete("/api/user/orders/{orderId}", newOrder.getId())
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(testUserId), 1800, false, false))
						.with(csrf()))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

		ApiErrorDTO error = objectMapper.readValue(response, ApiErrorDTO.class);

		assertEquals("El tiempo límite para anular el pedido (20 minutos) ha finalizado.", error.errorMsg());

		logger.info("Update order test: order successfully NOT deleted due to time limit constraint");
	}

	@Test
	public void givenOrderDelete_whenDelete_thenDeleteOrder() throws Exception {
		logger.info("Update order test: delete order");

		// create user test subject

		long testUserId = createUserTestSubject(new RegisterDTO(
				"OrderTestsUser",
				"OrderTestDeleteOrder@gmail.com",
				"OrderTestDeleteOrder@gmail.com",
				"password",
				"password"));

		// create JWT token

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"OrderTestDeleteOrder@gmail.com",
				testUserId,
				"USER");

		// create order test subject

		OrderDTO newOrder = createOrderTestSubject(0, testUserId, addressId, testSubjectOrderDetails, testSubjectCart, validAccessToken);

		// delete order test subject

		mockMvc.perform(delete("/api/user/orders/{orderId}?userId=1", newOrder.getId())
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(testUserId), 1800, false, false))
						.with(csrf()))
				.andExpect(status().isOk());

		logger.info("Update order test: order successfully deleted order");
	}

	@Test
	public void givenUserAccountDelete_whenUpdatingOrders_thenSetOrderUserIdToNull() throws Exception {
		logger.info("Update order test: set order's user id to null if user deletes account");

		// create user test subject

		long testUserId = createUserTestSubject(new RegisterDTO(
				"OrderTestsUser",
				"OrderTestUpdateSetOrderUserIdToNull@gmail.com",
				"OrderTestUpdateSetOrderUserIdToNull@gmail.com",
				"password",
				"password"));

		// create JWT token

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"OrderTestUpdateSetOrderUserIdToNull@gmail.com",
				testUserId,
				"USER");

		// create order test subject

		OrderDTO newOrder = createOrderTestSubject(0, testUserId, addressId, testSubjectOrderDetails, testSubjectCart, validAccessToken);

		// delete user test subject

		PasswordDTO passwordDTO = new PasswordDTO("password");

		mockMvc.perform(delete("/api/user/{userId}", testUserId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(passwordDTO))
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(testUserId), 30, false, false))
						.with(csrf()))
				.andExpect(status().isOk());

		// assert

		OrderDTO theOrder = findOrder(newOrder.getId(), testUserId, validAccessToken);
		assertNull(theOrder.getUser());

		logger.info("Update order test: successfully set order's user id to null after user deleted account");
	}
}