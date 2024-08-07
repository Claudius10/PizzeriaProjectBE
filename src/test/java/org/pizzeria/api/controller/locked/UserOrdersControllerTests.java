package org.pizzeria.api.controller.locked;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pizzeria.api.configs.security.utils.SecurityCookieUtils;
import org.pizzeria.api.configs.security.utils.SecurityTokenUtils;
import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.entity.dto.auth.RegisterDTO;
import org.pizzeria.api.entity.order.Cart;
import org.pizzeria.api.entity.order.OrderDetails;
import org.pizzeria.api.entity.order.OrderItem;
import org.pizzeria.api.entity.order.dto.NewUserOrderDTO;
import org.pizzeria.api.entity.order.dto.OrderDTO;
import org.pizzeria.api.entity.order.dto.UpdateUserOrderDTO;
import org.pizzeria.api.entity.role.Role;
import org.pizzeria.api.entity.user.User;
import org.pizzeria.api.entity.user.dto.PasswordDTO;
import org.pizzeria.api.repos.address.AddressRepository;
import org.pizzeria.api.repos.order.OrderRepository;
import org.pizzeria.api.repos.role.RoleRepository;
import org.pizzeria.api.repos.user.UserRepository;
import org.pizzeria.api.utils.globals.ApiResponses;
import org.pizzeria.api.utils.globals.ValidationResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@DirtiesContext
class UserOrdersControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private SecurityTokenUtils securityTokenUtils;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderRepository orderRepository;

	@AfterEach
	void cleanUp() {
		orderRepository.deleteAll();
		userRepository.deleteAll();
		addressRepository.deleteAll();
		roleRepository.deleteAll();
	}

	@Test
	void givenPostApiCallToCreateOrder_thenCreateOrder() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create address in database
		Long addressId = createAddressTestSubject("Test", 1);

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		// create DTO object
		Cart cart = new Cart.Builder()
				.withOrderItems(List.of(new OrderItem.Builder()
						.withWithName("Pepperoni")
						.withFormat("Familiar")
						.withProductType("Pizza")
						.withQuantity(1)
						.withPrice(18.30)
						.build()))
				.withTotalQuantity(1)
				.withTotalCost(18.30)
				.build();

		OrderDetails orderDetails = new OrderDetails.Builder()
				.withDeliveryHour("ASAP")
				.withPaymentType("Card")
				.build();

		NewUserOrderDTO newUserOrderDTO = new NewUserOrderDTO(userId, addressId, orderDetails, cart);

		// post api call to create user order
		MockHttpServletResponse response = mockMvc.perform(post("/api/user/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newUserOrderDTO))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false))
						.with(csrf()))
				.andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
	}

	@Test
	void givenPostApiCallToCreateOrder_whenCartIsEmpty_thenReturnBadRequestWithMessage() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create address in database
		Long addressId = createAddressTestSubject("Test", 1);

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		OrderDetails orderDetails = new OrderDetails.Builder()
				.withDeliveryHour("ASAP")
				.withPaymentType("Card")
				.build();

		NewUserOrderDTO newUserOrderDTO = new NewUserOrderDTO(userId, addressId, orderDetails, null);

		// post api call to create user order
		MockHttpServletResponse response = mockMvc.perform(post("/api/user/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newUserOrderDTO))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false))
						.with(csrf()))
				.andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getContentAsString()).isEqualTo(ValidationResponses.CART_IS_EMPTY);
	}

	@Test
	void givenGetApiCallToFindOrder_thenReturnOrder() throws Exception {

		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create address in database
		Long addressId = createAddressTestSubject("Test", 1);

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		// create DTO object
		Cart cart = new Cart.Builder()
				.withOrderItems(List.of(new OrderItem.Builder()
						.withWithName("Pepperoni")
						.withFormat("Familiar")
						.withProductType("Pizza")
						.withQuantity(1)
						.withPrice(18.30)
						.build()))
				.withTotalQuantity(1)
				.withTotalCost(18.30)
				.build();

		OrderDetails orderDetails = new OrderDetails.Builder()
				.withDeliveryHour("ASAP")
				.withPaymentType("Card")
				.build();

		NewUserOrderDTO newUserOrderDTO = new NewUserOrderDTO(userId, addressId, orderDetails, cart);

		// post api call to create user order
		MockHttpServletResponse response = mockMvc.perform(post("/api/user/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newUserOrderDTO))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false))
						.with(csrf()))
				.andReturn().getResponse();

		Long orderId = Long.valueOf(response.getContentAsString());

		// Act

		// get api call to find user order
		MockHttpServletResponse getResponse = mockMvc.perform(get("/api/user/orders/{orderId}", orderId)
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(getResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
		OrderDTO order = objectMapper.readValue(getResponse.getContentAsString(), OrderDTO.class);
		assertThat(order.getId()).isEqualTo(orderId);
	}

	@Test
	void givenGetApiCallToFindOrder_whenOrderNotFound_thenReturnAcceptedWithMessage() throws Exception {

		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		// Act

		// get api call to find user order
		MockHttpServletResponse response = mockMvc.perform(get("/api/user/orders/{orderId}", 99)
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());
		assertThat(response.getContentAsString()).isEqualTo(String.format(ApiResponses.ORDER_NOT_FOUND, 99));
	}

	@Test
	void givenOrderUpdate_whenNewAddress_thenReturnOrderWithUpdatedAddress() throws Exception {

		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create address in database
		Long addressId = createAddressTestSubject("Test", 1);

		// create address in database
		Long newAddressId = createAddressTestSubject("Test", 2);

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		// create user order
		OrderDTO order = createUserOrderTestSubject(0, userId, addressId, accessToken);

		// create UserOrderUpdate DTO
		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				order.getId(),
				userId,
				newAddressId,
				order.getCreatedOn(),
				order.getOrderDetails(),
				order.getCart());

		// Act

		// put api call to update order
		MockHttpServletResponse response = mockMvc.perform(put("/api/user/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 30, false, false))
						.with(csrf()))
				.andReturn().getResponse();

		// Assert

		Long orderId = Long.valueOf(response.getContentAsString());
		OrderDTO updatedOrder = findOrder(orderId, userId, accessToken);

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(updatedOrder.getAddress().getId()).isEqualTo(newAddressId);
	}

	@Test
	void givenOrderUpdate_whenNewOrderDetails_thenReturnOrderWithUpdatedOrderDetails() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create address in database
		Long addressId = createAddressTestSubject("Test", 1);

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		// create user order
		OrderDTO order = createUserOrderTestSubject(0, userId, addressId, accessToken);

		// create UserOrderUpdate DTO
		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				order.getId(),
				userId,
				addressId,
				order.getCreatedOn(),
				new OrderDetails.Builder()
						.withId(order.getId())
						.withDeliveryHour("Soon")
						.withPaymentType("Cash")
						.build(),
				order.getCart());

		// Act

		// put api call to update order
		MockHttpServletResponse response = mockMvc.perform(put("/api/user/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 30, false, false))
						.with(csrf()))
				.andReturn()
				.getResponse();

		// Assert

		Long orderId = Long.valueOf(response.getContentAsString());
		OrderDTO updatedOrder = findOrder(orderId, userId, accessToken);

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(updatedOrder.getOrderDetails().contentEquals(orderUpdate.getOrderDetails())).isTrue();
	}

	@Test
	void givenOrderUpdate_whenNewCart_thenReturnOrderWithUpdatedCart() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create address in database
		Long addressId = createAddressTestSubject("Test", 1);

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		// create user order
		OrderDTO order = createUserOrderTestSubject(0, userId, addressId, accessToken);

		// create UserOrderUpdate DTO
		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				order.getId(),
				userId,
				addressId,
				order.getCreatedOn(),
				order.getOrderDetails(),
				new Cart.Builder()
						.withOrderItems(List.of(new OrderItem.Builder()
								.withQuantity(1)
								.withProductType("Pizza")
								.withFormat("Mediana")
								.withWithName("Carbonara")
								.withPrice(14.75)
								.build()))
						.withId(order.getId())
						.withTotalQuantity(1)
						.withTotalCost(14.75)
						.build());

		// Act

		// put api call to update order
		MockHttpServletResponse response = mockMvc.perform(put("/api/user/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 30, false, false))
						.with(csrf()))
				.andReturn()
				.getResponse();

		// Assert

		Long orderId = Long.valueOf(response.getContentAsString());
		OrderDTO updatedOrder = findOrder(orderId, userId, accessToken);

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(updatedOrder.getCart().contentEquals(orderUpdate.getCart())).isTrue();
	}

	@Test
	void givenOrderUpdate_whenNewCartAndCartUpdateTimeLimitPassed_thenReturnOrderWithOriginalCart() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create address in database
		Long addressId = createAddressTestSubject("Test", 1);

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		// create user order
		int minutesInThePast = 11;
		OrderDTO order = createUserOrderTestSubject(minutesInThePast, userId, addressId, accessToken);

		// create UserOrderUpdate DTO
		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				order.getId(),
				userId,
				addressId,
				order.getCreatedOn(),
				order.getOrderDetails(),
				new Cart.Builder()
						.withOrderItems(List.of(new OrderItem.Builder()
								.withQuantity(1)
								.withProductType("Pizza")
								.withFormat("Mediana")
								.withWithName("Carbonara")
								.withPrice(14.75)
								.build()))
						.withId(order.getId())
						.withTotalQuantity(1)
						.withTotalCost(14.75)
						.build());

		// Act

		// put api call to update order
		MockHttpServletResponse response = mockMvc.perform(put("/api/user/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 30, false, false))
						.with(csrf()))
				.andReturn()
				.getResponse();

		// Assert

		Long orderId = Long.valueOf(response.getContentAsString());
		OrderDTO updatedOrder = findOrder(orderId, userId, accessToken);

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(updatedOrder.getCart().contentEquals(order.getCart())).isTrue();
	}

	@Test
	void givenOrderUpdate_whenOrderUpdateTimeLimitPassed_thenReturnBadRequestWithMessage() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create address in database
		Long addressId = createAddressTestSubject("Test", 1);

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		// create user order
		int minutesInThePast = 16;
		OrderDTO order = createUserOrderTestSubject(minutesInThePast, userId, addressId, accessToken);

		// create UserOrderUpdate DTO
		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				order.getId(),
				userId,
				addressId,
				order.getCreatedOn(),
				order.getOrderDetails(),
				new Cart.Builder()
						.withOrderItems(List.of(new OrderItem.Builder()
								.withQuantity(1)
								.withProductType("Pizza")
								.withFormat("Mediana")
								.withWithName("Carbonara")
								.withPrice(14.75)
								.build()))
						.withId(order.getId())
						.withTotalQuantity(1)
						.withTotalCost(14.75)
						.build());

		// Act

		// put api call to update order
		MockHttpServletResponse response = mockMvc.perform(put("/api/user/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 30, false, false))
						.with(csrf()))
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getContentAsString()).isEqualTo(ValidationResponses.ORDER_UPDATE_TIME_ERROR);
	}

	@Test
	void givenOrderUpdate_whenOrderNotFound_thenReturnAcceptedWithMessage() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create address in database
		Long addressId = createAddressTestSubject("Test", 1);

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		// create user order
		OrderDTO order = createUserOrderTestSubject(0, userId, addressId, accessToken);

		// create UserOrderUpdate DTO
		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				9879789L,
				userId,
				addressId,
				order.getCreatedOn(),
				order.getOrderDetails(),
				new Cart.Builder()
						.withOrderItems(List.of(new OrderItem.Builder()
								.withQuantity(1)
								.withProductType("Pizza")
								.withFormat("Mediana")
								.withWithName("Carbonara")
								.withPrice(14.75)
								.build()))
						.withId(order.getId())
						.withTotalQuantity(1)
						.withTotalCost(14.75)
						.build());

		// Act

		// put api call to update order
		MockHttpServletResponse response = mockMvc.perform(put("/api/user/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 30, false, false))
						.with(csrf()))
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());
		assertThat(response.getContentAsString()).isEqualTo(String.format(ApiResponses.UPDATE_USER_ORDER_ERROR,
				9879789, addressId));
	}

	@Test
	void givenOrderUpdate_whenAddressNotFound_thenReturnAcceptedWithMessage() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create address in database
		Long addressId = createAddressTestSubject("Test", 1);

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		// create user order
		OrderDTO order = createUserOrderTestSubject(0, userId, addressId, accessToken);

		// create UserOrderUpdate DTO
		UpdateUserOrderDTO orderUpdate = new UpdateUserOrderDTO(
				order.getId(),
				userId,
				878678L,
				order.getCreatedOn(),
				order.getOrderDetails(),
				new Cart.Builder()
						.withOrderItems(List.of(new OrderItem.Builder()
								.withQuantity(1)
								.withProductType("Pizza")
								.withFormat("Mediana")
								.withWithName("Carbonara")
								.withPrice(14.75)
								.build()))
						.withId(order.getId())
						.withTotalQuantity(1)
						.withTotalCost(14.75)
						.build());

		// Act

		// put api call to update order
		MockHttpServletResponse response = mockMvc.perform(put("/api/user/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderUpdate))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 30, false, false))
						.with(csrf()))
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());
		assertThat(response.getContentAsString()).isEqualTo(String.format(ApiResponses.UPDATE_USER_ORDER_ERROR,
				order.getId(), 878678));
	}

	@Test
	void givenOrderDelete_whenWithinTimeLimit_thenReturnDeletedOrderId() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create address in database
		Long addressId = createAddressTestSubject("Test", 1);

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		// create user order
		int minutesInThePast = 0;
		OrderDTO order = createUserOrderTestSubject(minutesInThePast, userId, addressId, accessToken);

		// Act

		// delete api call to delete order
		MockHttpServletResponse response = mockMvc.perform(delete("/api/user/orders/{orderId}", order.getId())
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 30, false, false))
						.with(csrf()))
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(Long.valueOf(response.getContentAsString())).isEqualTo(order.getId());
	}

	@Test
	void givenOrderDelete_whenTimeLimitPassed_thenReturnBadRequestWithMessage() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create address in database
		Long addressId = createAddressTestSubject("Test", 1);

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		// create user order
		int minutesInThePast = 21;
		OrderDTO order = createUserOrderTestSubject(minutesInThePast, userId, addressId, accessToken);

		// Act

		// delete api call to delete order
		MockHttpServletResponse response = mockMvc.perform(delete("/api/user/orders/{orderId}", order.getId())
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 30, false, false))
						.with(csrf()))
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getContentAsString()).isEqualTo(ValidationResponses.ORDER_DELETE_TIME_ERROR);
	}

	@Test
	void givenOrderDelete_whenOrderNotFound_thenReturnAcceptedWithMessage() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		// Act

		// delete api call to delete order
		MockHttpServletResponse response = mockMvc.perform(delete("/api/user/orders/{orderId}", 995678)
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 30, false, false))
						.with(csrf()))
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());
		assertThat(response.getContentAsString()).isEqualTo(String.format(ApiResponses.ORDER_NOT_FOUND, 995678));
	}

	@Test
	void givenGetUserOrderSummary_thenReturnUserOrderSummary() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create address in database
		Long addressId = createAddressTestSubject("Test", 1);

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		// create user order
		int minutesInThePast = 0;
		createUserOrderTestSubject(minutesInThePast, userId, addressId, accessToken);

		int pageSize = 1;
		int pageNumber = 0;

		// Act

		// get api call to get OrderSummary
		MockHttpServletResponse response = mockMvc.perform(get("/api/user/orders?pageNumber={pN}&pageSize={pS}&userId={userId}",
						pageNumber, pageSize, userId)
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 30, false, false)))
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	void givenGetUserOrderSummary_whenNoOrders_thenReturnAcceptedWithMessage() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		int pageSize = 1;
		int pageNumber = 0;

		// Act

		// get api call to get OrderSummary
		MockHttpServletResponse response = mockMvc.perform(get("/api/user/orders?pageNumber={pN}&pageSize={pS}&userId={userId}",
						pageNumber, pageSize, userId)
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 30, false, false)))
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());
		assertThat(response.getContentAsString()).isEqualTo(ApiResponses.ORDER_LIST_EMPTY);
	}

	@Test
	void givenGetUserOrderSummary_whenUserNotFound_thenReturnUnauthorizedWithMessage() throws Exception {
		// Arrange

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				0L,
				"USER");

		int pageSize = 1;
		int pageNumber = 0;

		// Act

		// get api call to get OrderSummary
		MockHttpServletResponse response = mockMvc.perform(get("/api/user/orders?pageNumber={pN}&pageSize={pS}&userId={userId}",
						pageNumber, pageSize, 0)
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(0), 30, false, false)))
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		assertThat(response.getContentAsString()).isEqualTo(String.format(ApiResponses.USER_NOT_FOUND, 0));
	}

	@Test
	void givenDeleteUserApiCall_whenUserHasOrders_thenDeleteUserAndUserIdFromOrders() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create address in database
		Long addressId = createAddressTestSubject("Test", 1);

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		// create user order
		int minutesInThePast = 0;
		OrderDTO orderDTO = createUserOrderTestSubject(minutesInThePast, userId, addressId, accessToken);

		assertThat(userId).isEqualTo(orderDTO.getUser().id());

		// delete the user

		// create dto object
		PasswordDTO passwordDTO = new PasswordDTO("Password1");

		// put api call to delete the user
		MockHttpServletResponse response = mockMvc.perform(delete("/api/user/{userId}", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(passwordDTO))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 1800, false, false))
						.with(csrf()))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		Optional<User> user = userRepository.findUserByEmailWithRoles("Tester@gmail.com");
		assertThat(user).isEmpty();

		OrderDTO orderDTOAfterUserDeleted = findOrder(orderDTO.getId(), userId, accessToken);
		assertThat(orderDTOAfterUserDeleted.getUser()).isNull();
	}

	OrderDTO findOrder(Long orderId, long userId, String validAccessToken) throws Exception {
		String response = mockMvc.perform(get("/api/user/orders/{orderId}", orderId)
				.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
				.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 1800, false, false))
				.with(csrf())).andReturn().getResponse().getContentAsString();
		return objectMapper.readValue(response, OrderDTO.class);
	}

	OrderDTO createUserOrderTestSubject(int minutesInThePast, long userId, long addressId, String validAccessToken) throws Exception {
		Cart cart = new Cart.Builder()
				.withOrderItems(List.of(new OrderItem.Builder()
						.withWithName("Pepperoni")
						.withFormat("Familiar")
						.withProductType("Pizza")
						.withQuantity(1)
						.withPrice(18.30)
						.build()))
				.withTotalQuantity(1)
				.withTotalCost(18.30)
				.build();

		OrderDetails orderDetails = new OrderDetails.Builder()
				.withDeliveryHour("ASAP")
				.withPaymentType("Card")
				.build();

		NewUserOrderDTO newUserOrderDTO = new NewUserOrderDTO(userId, addressId, orderDetails, cart);

		// post api call to create user order
		MockHttpServletResponse response = mockMvc.perform(post("/api/user/orders/tests?minusMin={minutesInThePast}", minutesInThePast)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newUserOrderDTO))
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 1800, false, false))
						.with(csrf()))
				.andReturn().getResponse();

		Long orderId = Long.valueOf(response.getContentAsString());
		return findOrder(orderId, userId, validAccessToken);
	}

	Long createUserTestSubject() throws Exception {
		if (roleRepository.findByName("USER") == null) {
			roleRepository.save(new Role("USER"));
		}

		return Long.valueOf(mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new RegisterDTO(
								"Tester",
								"Tester@gmail.com",
								"Tester@gmail.com",
								"Password1",
								"Password1")))
						.with(csrf()))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString());
	}

	Long createAddressTestSubject(String streetName, int streetNumber) {
		return addressRepository.save(
						new Address.Builder()
								.withStreet(streetName)
								.withStreetNr(streetNumber)
								.build())
				.getId();
	}
}