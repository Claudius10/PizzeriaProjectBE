package PizzaApp.api.controller.locked;

import PizzaApp.api.configs.security.utils.SecurityCookieUtils;
import PizzaApp.api.configs.security.utils.SecurityTokenUtils;
import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.OrderItem;
import PizzaApp.api.entity.order.dto.NewUserOrderDTO;
import PizzaApp.api.entity.order.dto.OrderDTO;
import PizzaApp.api.entity.order.dto.UpdateUserOrderDTO;
import PizzaApp.api.entity.role.Role;
import PizzaApp.api.repos.address.AddressRepository;
import PizzaApp.api.repos.order.OrderRepository;
import PizzaApp.api.repos.role.RoleRepository;
import PizzaApp.api.repos.user.UserRepository;
import PizzaApp.api.utils.globals.SecurityResponses;
import PizzaApp.api.utils.globals.ValidationResponses;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@DirtiesContext
public class UserOrdersControllerTests {

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
	public void cleanUp() {
		orderRepository.deleteAll();
		userRepository.deleteAll();
		addressRepository.deleteAll();
		roleRepository.deleteAll();
	}

	@Test
	public void givenGetApiCallToFindOrder_thenReturnOrder() throws Exception {

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

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		OrderDTO order = objectMapper.readValue(getResponse.getContentAsString(), OrderDTO.class);
		assertThat(order.getId()).isEqualTo(orderId);
	}

	@Test
	public void givenGetApiCallToFindOrder_whenOrderNotFound_thenReturnNotFound() throws Exception {

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

		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void givenOrderUpdate_whenNewAddress_thenReturnOrderWithUpdatedAddress() throws Exception {

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
	public void givenOrderUpdate_whenNewOrderDetails_thenReturnOrderWithUpdatedOrderDetails() throws Exception {
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
		assertThat(updatedOrder.getOrderDetails().contentEquals(orderUpdate.getOrderDetails())).isEqualTo(true);
	}

	@Test
	public void givenOrderUpdate_whenNewCart_thenReturnOrderWithUpdatedCart() throws Exception {
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
		assertThat(updatedOrder.getCart().contentEquals(orderUpdate.getCart())).isEqualTo(true);
	}

	@Test
	public void givenOrderUpdate_whenNewCartAndCartUpdateTimeLimitPassed_thenReturnOrderWithOriginalCart() throws Exception {
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
		assertThat(updatedOrder.getCart().contentEquals(order.getCart())).isEqualTo(true);
	}

	@Test
	public void givenOrderUpdate_whenOrderUpdateTimeLimitPassed_thenReturnBadRequestWithMessage() throws Exception {
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
	public void givenOrderDelete_whenWithinTimeLimit_thenReturnDeletedOrderId() throws Exception {
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
	public void givenOrderDelete_whenTimeLimitPassed_thenReturnBadRequestWithMessage() throws Exception {
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
	public void givenOrderDelete_whenOrderNotFound_thenReturnNotFound() throws Exception {
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
		MockHttpServletResponse response = mockMvc.perform(delete("/api/user/orders/{orderId}", 99)
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 30, false, false))
						.with(csrf()))
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
		assertThat(response.getContentAsString()).isEqualTo(String.format(ValidationResponses.ORDER_NOT_FOUND, 99));
	}

	@Test
	public void givenGetUserOrderSummary_thenReturnUserOrderSummary() throws Exception {
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
	public void givenGetUserOrderSummary_whenNoOrders_thenReturnNotFound() throws Exception {
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

		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void givenGetUserOrderSummary_whenUserNotFound_thenHandleException() throws Exception {
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
		assertThat(response.getContentAsString()).isEqualTo(String.format(SecurityResponses.USER_NOT_FOUND, 0));
	}

	public OrderDTO findOrder(Long orderId, long userId, String validAccessToken) throws Exception {
		String response = mockMvc.perform(get("/api/user/orders/{orderId}", orderId)
				.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
				.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 1800, false, false))
				.with(csrf())).andReturn().getResponse().getContentAsString();
		return objectMapper.readValue(response, OrderDTO.class);
	}

	public OrderDTO createUserOrderTestSubject(int minutesInThePast, long userId, long addressId, String validAccessToken) throws Exception {
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

	public Long createUserTestSubject() throws Exception {
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
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
	}

	public Long createAddressTestSubject(String streetName, int streetNumber) {
		return addressRepository.save(
						new Address.Builder()
								.withStreet(streetName)
								.withStreetNr(streetNumber)
								.build())
				.getId();
	}
}