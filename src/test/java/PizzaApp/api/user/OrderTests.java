package PizzaApp.api.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;

import PizzaApp.api.entity.dto.user.NewUserOrderDTO;
import PizzaApp.api.entity.dto.user.UpdateUserOrderDTO;
import PizzaApp.api.entity.dto.user.UserOrderDataDTO;
import PizzaApp.api.services.order.OrderService;
import PizzaApp.api.utility.auth.CookieUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import PizzaApp.api.entity.order.cart.Cart;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.OrderItem;
import PizzaApp.api.exceptions.exceptions.order.OrderDataUpdateTimeLimitException;
import PizzaApp.api.exceptions.exceptions.order.OrderDeleteTimeLimitException;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class OrderTests {

	private final Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtEncoder jwtEncoder;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OrderService ordersService;

	// test objects
	private final OrderDetails originalOrderDetails = new OrderDetails.Builder()
			.withDeliveryHour("ASAP")
			.withPaymentType("Credit Card")
			.build();

	private final Cart originalCart = new Cart.Builder()
			.withTotalQuantity(1)
			.withTotalCost(14.75)
			.withTotalCostOffers(0D)
			.withOrderItems(List.of(new OrderItem.Builder()
							.withProductType("pizza")
							.withWithName("Allo Bianca")
							.withFormat("Mediana")
							.withQuantity(1)
							.withPrice(14.75)
							.build(),
					new OrderItem.Builder()
							.withProductType("pizza")
							.withWithName("Cuatro Quesos")
							.withFormat("Mediana")
							.withQuantity(1)
							.withPrice(14.75)
							.build()))
			.build();

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


	private LocalDateTime createdOn;
	private Long orderId, orderIdMinus11Mins, orderIdMinus16Mins, orderIdMinus21Mins, orderToDelete;

	private String validAccessToken, formattedCreatedOn;

	@BeforeAll
	public void setup() {
		validAccessToken = jwtEncoder.encode(
						JwtEncoderParameters.from(
								JwtClaimsSet.builder()
										.issuer("https://pizzeriaprojectbe-production.up.railway.app")
										.subject("test subject")
										.issuedAt(Instant.now())
										.expiresAt(Instant.now().plus(30, ChronoUnit.SECONDS))
										.claim("roles", "USER")
										.claim("id", 1)
										.build()))
				.getTokenValue();

		orderIdMinus11Mins = createTestSubject(11);
		orderIdMinus16Mins = createTestSubject(16);
		orderIdMinus21Mins = createTestSubject(21);
		orderToDelete = createTestSubject(0);
	}

	@Test
	@org.junit.jupiter.api.Order(1)
	public void givenUserOrder_whenCreate_thenReturnOrder() throws Exception {
		logger.info("Update order test: create original user order");

		NewUserOrderDTO order = new NewUserOrderDTO(
				new UserOrderDataDTO(
						1L,
						1L,
						111222333,
						"clau@gmail.com",
						"Clau"),
				originalOrderDetails,
				originalCart);

		// action: persist and retrieve order
		Order dbOrder = ordersService.findById(Long.valueOf(mockMvc.perform(post("/api/user/orders/{userId}", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order))
						.cookie(CookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.cookie(CookieUtils.makeCookie("id", "1", 30, false, false))
						.with(csrf()))
				.andReturn().getResponse().getContentAsString()));

		// *** set goodies that will be useful later
		orderId = dbOrder.getId();
		createdOn = dbOrder.getCreatedOn();
		formattedCreatedOn = dbOrder.getFormattedCreatedOn();

		originalOrderDetails.setId(orderId);
		originalCart.setId(orderId);
		// ***

		// then expect/assert: returned data matches set data
		assertAll("Data returned matches expected values",
				() -> assertEquals(dbOrder.getUserData().getId(), order.userOrderData().userId()),
				() -> assertEquals(dbOrder.getAddress().getId(), order.userOrderData().addressId()),
				() -> assertEquals(dbOrder.getContactTel(), order.userOrderData().tel()),
				() -> assertEquals(dbOrder.getEmail(), order.userOrderData().email()),
				() -> assertEquals(dbOrder.getCustomerName(), order.userOrderData().customerName()),
				() -> assertEquals(dbOrder.getCart().getId(), order.cart().getId())
		);

		logger.info("Update order test: successfully created original user order");
	}

	@Test
	public void givenNewUserData_whenUpdate_thenReturnOrderWithNewData() throws Exception {
		logger.info("Update order test: update user data");

		// given / preparation:
		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				orderId,
				createdOn,
				formattedCreatedOn,
				new UserOrderDataDTO(
						1L,
						1L,
						666333999,
						"newEmail@gmail.com",
						"newCustomer"),
				originalOrderDetails,
				originalCart);

		// action: persist and retrieve order
		Order dbOrder = ordersService.findById(Long.valueOf(mockMvc.perform(put("/api/user/orders/{userId}", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(CookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.cookie(CookieUtils.makeCookie("id", "1", 30, false, false))
						.with(csrf()))
				.andReturn().getResponse().getContentAsString()));

		// then expect/assert: returned data matches set data
		assertAll("Data returned matches expected values",
				() -> assertEquals(dbOrder.getId(), orderUpdate.orderId()),
				() -> assertEquals(dbOrder.getCustomerName(), orderUpdate.userOrderData().customerName()),
				() -> assertEquals(dbOrder.getContactTel(), orderUpdate.userOrderData().tel()),
				() -> assertEquals(dbOrder.getEmail(), orderUpdate.userOrderData().email()));

		logger.info("Update order test: successfully updated user data");
	}

	@Test
	public void givenNewAddress_whenUpdate_thenReturnOrderWithUpdatedAddress() throws Exception {
		logger.info("Update order test: update address");

		// given / preparation:
		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				orderId,
				createdOn,
				formattedCreatedOn,
				new UserOrderDataDTO(
						1L,
						2L,
						666333999,
						"newEmail@gmail.com",
						"newCustomer"),
				originalOrderDetails,
				originalCart);

		// action: persist and retrieve order
		Order dbOrder = ordersService.findById(Long.valueOf(mockMvc.perform(put("/api/user/orders/{userId}", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(CookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.cookie(CookieUtils.makeCookie("id", "1", 30, false, false))
						.with(csrf()))
				.andReturn().getResponse().getContentAsString()));

		// then expect/assert: returned data matches set data
		assertAll("Data returned matches expected values",
				() -> assertEquals(dbOrder.getId(), orderUpdate.orderId()),
				() -> assertEquals(dbOrder.getAddress().getId(), orderUpdate.userOrderData().addressId())
		);
		logger.info("Update order test: successfully updated address");
	}

	@Test
	public void givenOrderDetails_whenUpdate_thenReturnOrderWithNewOrderDetails() throws Exception {
		logger.info("Update order test: update orderDetails");

		// given / preparation:
		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				orderId,
				createdOn,
				formattedCreatedOn,
				new UserOrderDataDTO(
						1L,
						2L,
						666333999,
						"newEmail@gmail.com",
						"newCustomer"),
				new OrderDetails.Builder()
						.withId(orderId)
						.withPaymentType("Efectivo")
						.withChangeRequested(15D)
						.withDeliveryHour("15:30")
						.withDeliveryComment("Well cut and hot")
						.build(),
				originalCart);

		// action: persist and retrieve order
		Order dbOrder = ordersService.findById(Long.valueOf(mockMvc.perform(put("/api/user/orders/{userId}", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(CookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.cookie(CookieUtils.makeCookie("id", "1", 30, false, false))
						.with(csrf()))
				.andReturn().getResponse().getContentAsString()));

		// then expect/assert: returned data matches set data
		assertAll("Data returned matches expected values",
				() -> assertEquals(dbOrder.getId(), orderUpdate.orderId()),
				() -> assertTrue(dbOrder.getOrderDetails().entityEquals(orderUpdate.orderDetails()))
		);
		logger.info("Update order test: successfully updated orderDetails");
	}

	@Test
	@org.junit.jupiter.api.Order(2)
	public void givenCart_whenUpdate_thenReturnOrderWithNewCart() throws Exception {
		logger.info("Update order test: update cart");

		// given / preparation:
		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				orderId,
				createdOn,
				formattedCreatedOn,
				new UserOrderDataDTO(
						1L,
						1L,
						111222333,
						"clau@gmail.com",
						"Clau"),
				new OrderDetails.Builder()
						.withId(orderId)
						.withPaymentType("Efectivo")
						.withChangeRequested(30D)
						.withDeliveryHour("15:30")
						.withDeliveryComment("Well cut and hot")
						.build(),
				new Cart.Builder()
						.withId(orderId)
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

		// action: persist and retrieve order
		Order dbOrder = ordersService.findById(Long.valueOf(mockMvc.perform(put("/api/user/orders/{userId}", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(CookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.cookie(CookieUtils.makeCookie("id", "1", 30, false, false))
						.with(csrf()))
				.andReturn().getResponse().getContentAsString()));

		// then expect/assert: returned data matches set data
		assertAll("Data returned should match data set for update",
				() -> assertEquals(dbOrder.getId(), orderUpdate.orderId()),
				() -> assertTrue(dbOrder.getCart().entityEquals(orderUpdate.cart()))
		);
		logger.info("Update order test: successfully updated cart");
	}

	@Test
	public void givenCartUpdateAfterTimeLimit_whenUpdate_thenCorrectlySetNewCartToNullAndUseOriginalCart() throws Exception {
		logger.info("Update order test: update cart after time limit");

		// create order for update
		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				orderIdMinus11Mins,
				LocalDateTime.now().minusMinutes(11),
				LocalDateTime.now().minusMinutes(11).format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")),
				new UserOrderDataDTO(
						1L,
						1L,
						111222333,
						"clau@gmail.com",
						"Clau"),
				new OrderDetails.Builder()
						.withId(orderIdMinus11Mins)
						.withDeliveryHour("ASAP")
						.withPaymentType("Efectivo")
						.withDeliveryComment("Without oregano")
						.build(),
				new Cart.Builder()
						.withId(orderIdMinus11Mins)
						.withTotalQuantity(1)
						.withTotalCost(13.30)
						.withTotalCostOffers(0D)
						.withOrderItems(List.of(new OrderItem.Builder()
								.withProductType("pizza")
								.withWithName("Cuatro Quesos")
								.withFormat("Mediana")
								.withQuantity(1)
								.withPrice(13.30)
								.build()))
						.build());

		// action: persist and retrieve order
		Order dbOrder = ordersService.findById(Long.valueOf(mockMvc.perform(put("/api/user/orders/{userId}", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(CookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.cookie(CookieUtils.makeCookie("id", "1", 30, false, false))
						.with(csrf()))
				.andReturn().getResponse().getContentAsString()));

		// successful test returns testSubjectCart instead of the one set here
		// because cart update was over the time limit, so original cart was set back
		assertAll("Data returned matches expected values",
				() -> assertEquals(dbOrder.getId(), orderUpdate.orderId()),
				() -> assertTrue(dbOrder.getCart().entityEquals(testSubjectCart)),
				() -> assertTrue(dbOrder.getOrderDetails().entityEquals(testSubjectOrderDetails))
		);
		logger.info("Update order test: cart successfully NOT updated due to time limit constraint");
	}

	@Test
	public void givenOrderDataUpdateAfterTimeLimit_whenUpdate_thenThrowException() throws Exception {
		logger.info("Update order test: update order data after time limit");

		// create order for update
		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				orderIdMinus16Mins,
				LocalDateTime.now().minusMinutes(16),
				LocalDateTime.now().minusMinutes(16).format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")),
				new UserOrderDataDTO(
						1L,
						1L,
						111222333,
						"clau@gmail.com",
						"Clau"),
				new OrderDetails.Builder()
						.withId(orderIdMinus16Mins)
						.withDeliveryHour("22:30")
						.withPaymentType("Tarjeta")
						.withDeliveryHour("No llamar al timbre duerme el bebe")
						.build(),
				new Cart.Builder()
						.withId(orderIdMinus16Mins)
						.withTotalQuantity(1)
						.withTotalCost(13.30)
						.withTotalCostOffers(0D)
						.withOrderItems(List.of(new OrderItem.Builder()
								.withProductType("pizza")
								.withWithName("Cuatro Quesos")
								.withFormat("Mediana")
								.withQuantity(1)
								.withPrice(13.30)
								.build()))
						.build());

		// action: update order and then expect exception is thrown
		mockMvc.perform(put("/api/user/orders/{userId}", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(CookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.cookie(CookieUtils.makeCookie("id", "1", 30, false, false))
						.with(csrf()))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(OrderDataUpdateTimeLimitException.class)));

		logger.info("Update order test: order data successfully NOT updated due to time limit constraint");
	}

	@Test
	public void givenOrderDeleteAfterTimeLimit_whenDelete_thenThrowException() throws Exception {
		logger.info("Update order test: delete order after time limit");

		// action: update order
		mockMvc.perform(delete("/api/user/orders/{userId}/{orderId}", 1, orderIdMinus21Mins)
						.cookie(CookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.cookie(CookieUtils.makeCookie("id", "1", 30, false, false))
						.with(csrf()))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(OrderDeleteTimeLimitException.class)));

		logger.info("Update order test: order successfully NOT deleted due to time limit constraint");
	}

	@Test
	public void givenOrderDeleteWithInvalidUserId_whenDelete_thenThrowException() throws Exception {
		logger.info("Update order test: delete order with invalid user id");

		// action: update order
		mockMvc.perform(delete("/api/user/orders/{orderId}/{userId}", orderToDelete, 2)
						.cookie(CookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.cookie(CookieUtils.makeCookie("id", "1", 30, false, false))
						.with(csrf()))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(AccessDeniedException.class))).andDo(MockMvcResultHandlers.print());

		logger.info("Update order test: order successfully NOT deleted due to invalid user id");
	}

	@Test
	public void givenOrderDeleteWithValidUserId_whenDelete_thenDeleteOrder() throws Exception {
		logger.info("Update order test: delete order with valid user id");

		// action: update order
		mockMvc.perform(delete("/api/user/orders/{userId}/{orderId}", 1, orderToDelete)
						.cookie(CookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.cookie(CookieUtils.makeCookie("id", "1", 30, false, false))
						.with(csrf()))
				.andExpect(status().isAccepted());

		logger.info("Update order test: order successfully deleted order");
	}

	public Long createTestSubject(int minusMins) {
		NewUserOrderDTO order = new NewUserOrderDTO(
				new UserOrderDataDTO(
						1L,
						1L,
						111222333,
						"clau@gmail.com",
						"Clau"),
				testSubjectOrderDetails,
				new Cart.Builder()
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
						.build());
		return ordersService.createUserOrderTest(order, LocalDateTime.now().minusMinutes(minusMins));
	}
}