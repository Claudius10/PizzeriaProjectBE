package PizzaApp.api.controller;

import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.dto.error.ApiErrorDTO;
import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.OrderItem;
import PizzaApp.api.entity.order.dto.NewAnonOrderDTO;
import PizzaApp.api.repos.order.OrderRepository;
import PizzaApp.api.utils.globals.ValidationResponses;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class AnonControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OrderRepository orderRepository;

	@Test
	public void givenAnonOrderPostApiCall_whenAllOk_thenReturnOrder() throws Exception {
		// Act

		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(anonOrderStub(
								"customerName",
								111222333,
								"customerEmail@gmail.com",
								"Street",
								5,
								"Floor",
								"Door",
								null,
								"ASAP",
								"Cash",
								null,
								false))))
				.andReturn().getResponse().getContentAsString();

		// Assert

		long count = orderRepository.count();
		assertThat(count).isEqualTo(1);
	}

	@Test
	public void givenAnonOrderPostApiCall_whenInvalidCustomerName_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(anonOrderStub(
								"·$dfsaf3",
								111222333,
								"customerEmail@gmail.com",
								"Street",
								5,
								"Floor",
								"Door",
								null,
								"ASAP",
								"Cash",
								null,
								false))))
				.andExpect(result -> {
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("anonCustomerName");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.USER_NAME);
						}
				);
	}

	@Test
	public void givenAnonOrderPostApiCall_whenInvalidCustomerNumber_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(anonOrderStub(
								"customerName",
								1,
								"customerEmail@gmail.com",
								"Street",
								5,
								"Floor",
								"Door",
								null,
								"ASAP",
								"Cash",
								null,
								false))))
				.andExpect(result -> {
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("anonCustomerContactNumber");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.ANON_CUSTOMER_NUMBER);
						}
				);
	}

	@Test
	public void givenAnonOrderPostApiCall_whenInvalidCustomerEmail_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(anonOrderStub(
								"customerName",
								111222333,
								"dsajn$·~#",
								"Street",
								5,
								"Floor",
								"Door",
								null,
								"ASAP",
								"Cash",
								null,
								false))))
				.andExpect(result -> {
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("anonCustomerEmail");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.USER_EMAIL);
						}
				);
	}

	@Test
	public void givenAnonOrderPostApiCall_whenInvalidAddressStreet_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(anonOrderStub(
								"customerName",
								111222333,
								"anonCustomerEmail@gmail.com",
								"%$·%·",
								5,
								"Floor",
								"Door",
								null,
								"ASAP",
								"Cash",
								null,
								false))))
				.andExpect(result -> {
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("address.street");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.ADDRESS_STREET);
						}
				);
	}

	@Test
	public void givenAnonOrderPostApiCall_whenInvalidAddressStreetNumber_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(anonOrderStub(
								"customerName",
								111222333,
								"anonCustomerEmail@gmail.com",
								"Street",
								512313123,
								"Floor",
								"Door",
								null,
								"ASAP",
								"Cash",
								null,
								false))))
				.andExpect(result -> {
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("address.streetNr");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.ADDRESS_STREET_NUMBER);
						}
				);
	}

	@Test
	public void givenAnonOrderPostApiCall_whenInvalidAddressFloor_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(anonOrderStub(
								"customerName",
								111222333,
								"anonCustomerEmail@gmail.com",
								"Street",
								14,
								"Floor&%$",
								"Door",
								null,
								"ASAP",
								"Cash",
								null,
								false))))
				.andExpect(result -> {
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("address.floor");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.ADDRESS_FLOOR);
						}
				);
	}

	@Test
	public void givenAnonOrderPostApiCall_whenInvalidDeliveryHour_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(anonOrderStub(
								"customerName",
								111222333,
								"anonCustomerEmail@gmail.com",
								"Street",
								14,
								"Floor",
								"Door",
								null,
								null,
								"Cash",
								null,
								false))))
				.andExpect(result -> {
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("orderDetails.deliveryHour");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.ORDER_DETAILS_DELIVERY_HOUR);
						}
				);
	}

	@Test
	public void givenAnonOrderPostApiCall_whenInvalidPaymentType_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(anonOrderStub(
								"customerName",
								111222333,
								"anonCustomerEmail@gmail.com",
								"Street",
								14,
								"Floor",
								"Door",
								null,
								"ASAP",
								null,
								null,
								false))))
				.andExpect(result -> {
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("orderDetails.paymentType");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.ORDER_DETAILS_PAYMENT);
						}
				);
	}

	@Test
	public void givenAnonOrderPostApiCall_whenInvalidChangeRequest_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(anonOrderStub(
								"customerName",
								111222333,
								"anonCustomerEmail@gmail.com",
								"Street",
								14,
								"Floor",
								"Door",
								10D,
								"ASAP",
								"Cash",
								null,
								false))))
				.andExpect(result -> {
							String response = result.getResponse().getContentAsString();
							ApiErrorDTO apiError = objectMapper.readValue(response, ApiErrorDTO.class);
							assertThat(apiError.errorMsg()).isEqualTo(ValidationResponses.ORDER_DETAILS_CHANGE_REQUESTED);
						}
				);
	}

	@Test
	public void givenAnonOrderPostApiCall_whenInvalidComment_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(anonOrderStub(
								"customerName",
								111222333,
								"anonCustomerEmail@gmail.com",
								"Street",
								14,
								"Floor",
								"Door",
								null,
								"ASAP",
								"Cash",
								"%$·%·$",
								false))))
				.andExpect(result -> {
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("orderDetails.deliveryComment");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.ORDER_DETAILS_COMMENT);
						}
				);
	}

	@Test
	public void givenAnonOrderPostApiCall_whenCartIsEmpty_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(anonOrderStub(
								"customerName",
								111222333,
								"anonCustomerEmail@gmail.com",
								"Street",
								14,
								"Floor",
								"Door",
								null,
								"ASAP",
								"Cash",
								null,
								true))))
				.andExpect(result -> {
							String response = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
							ApiErrorDTO apiError = objectMapper.readValue(response, ApiErrorDTO.class);
							assertThat(apiError.errorMsg()).isEqualTo(ValidationResponses.CART_IS_EMPTY);
						}
				);
	}

	@Test
	public void givenRegisterPostApiCall_whenAllOk_thenRegister() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerStub(
								"UserToRegister",
								"emailRegister@gmail.com",
								"emailRegister@gmail.com",
								"Password1",
								"Password1")
						)))
				.andExpect(status().isOk());
	}

	@Test
	public void givenRegisterPostApiCall_whenInvalidUserName_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerStub(
								"UserToRegi·%$ster",
								"emailRegister@gmail.com",
								"emailRegister@gmail.com",
								"Password",
								"Password")
						)))
				.andExpect(result -> {
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("name");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.USER_NAME);
						}
				);
	}

	@Test
	public void givenRegisterPostApiCall_whenNonMatchingEmail_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerStub(
								"UserToRegister",
								"emailRegiste2@gmail.com",
								"emailRegister@gmail.com",
								"Password1",
								"Password1")
						)))
				.andExpect(result -> {
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("email");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.USER_EMAIL_MATCHING);
						}
				);
	}

	@Test
	public void givenRegisterPostApiCall_whenInvalidEmail_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerStub(
								"UserToRegister",
								"emailRegister$·%·$$gmail.com",
								"emailRegister$·%·$$gmail.com",
								"Password",
								"Password")
						)))
				.andExpect(result -> {
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("email");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.USER_EMAIL);
						}
				);
	}

	@Test
	public void givenRegisterPostApiCall_whenNonMatchingPassword_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerStub(
								"UserToRegister",
								"emailRegister@gmail.com",
								"emailRegister@gmail.com",
								"Password1",
								"Password13")
						)))
				.andExpect(result -> {
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("password");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.USER_PASSWORD_MATCHING);
						}
				);
	}

	@Test
	public void givenRegisterPostApiCall_whenInvalidPassword_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerStub(
								"UserToRegister",
								"emailRegister@gmail.com",
								"emailRegister@gmail.com",
								"Password",
								"Password")
						)))
				.andExpect(result -> {
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("password");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.USER_PASSWORD);
						}
				);
	}

	public RegisterDTO registerStub(String name, String email, String matchingEmail, String password, String matchingPassword) {
		return new RegisterDTO(name, email, matchingEmail, password, matchingPassword);
	}

	public NewAnonOrderDTO anonOrderStub(String customerName, int customerNumber, String customerEmail, String street,
										 int streetNumber, String floor, String door, Double changeRequested,
										 String deliveryHour, String paymentType, String comment,
										 boolean emptyCart) {
		Cart cartStub = new Cart.Builder()
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

		if (emptyCart) {
			cartStub = new Cart.Builder().withEmptyItemList().build();
		}

		return new NewAnonOrderDTO(
				customerName,
				customerNumber,
				customerEmail,
				new Address.Builder()
						.withStreet(street)
						.withStreetNr(streetNumber)
						.withFloor(floor)
						.withDoor(door)
						.build(),
				new OrderDetails.Builder()
						.withDeliveryHour(deliveryHour)
						.withPaymentType(paymentType)
						.withChangeRequested(changeRequested)
						.withDeliveryComment(comment)
						.build(),
				cartStub
		);
	}
}