package org.pizzeria.api.controller.open;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.entity.cart.Cart;
import org.pizzeria.api.entity.order.OrderDetails;
import org.pizzeria.api.entity.cart.CartItem;
import org.pizzeria.api.entity.order.dto.NewAnonOrderDTO;
import org.pizzeria.api.repos.order.OrderRepository;
import org.pizzeria.api.utils.globals.ValidationResponses;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@DirtiesContext
class AnonControllerAnonOrderTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OrderRepository orderRepository;

	@Test
	void givenAnonOrderPostApiCall_whenAllOk_thenReturnOrder() throws Exception {
		// Act

		// post api call to create anon order
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
	void givenAnonOrderPostApiCall_whenInvalidCustomerName_thenThrowException() throws Exception {
		// Act

		// post api call to create anon order
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
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("anonCustomerName");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.NAME_INVALID);
						}
				);
	}

	@Test
	void givenAnonOrderPostApiCall_whenInvalidCustomerNumber_thenThrowException() throws Exception {
		// Act

		// post api call to create anon order
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
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("anonCustomerContactNumber");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.NUMBER_INVALID);
						}
				);
	}

	@Test
	void givenAnonOrderPostApiCall_whenInvalidCustomerEmail_thenThrowException() throws Exception {
		// Act

		// post api call to create anon order
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
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("anonCustomerEmail");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.EMAIL_INVALID);
						}
				);
	}

	@Test
	void givenAnonOrderPostApiCall_whenInvalidAddressStreet_thenThrowException() throws Exception {
		// Act

		// post api call to create anon order
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
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("address.street");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.ADDRESS_STREET);
						}
				);
	}

	@Test
	void givenAnonOrderPostApiCall_whenInvalidAddressStreetNumber_thenThrowException() throws Exception {
		// Act

		// post api call to create anon order
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
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("address.streetNr");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.ADDRESS_STREET_NUMBER);
						}
				);
	}

	@Test
	void givenAnonOrderPostApiCall_whenInvalidAddressFloor_thenThrowException() throws Exception {
		// Act

		// post api call to create anon order
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
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("address.floor");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.ADDRESS_FLOOR);
						}
				);
	}

	@Test
	void givenAnonOrderPostApiCall_whenInvalidDeliveryHour_thenThrowException() throws Exception {
		// Act

		// post api call to create anon order
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
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("orderDetails.deliveryTime");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.ORDER_DETAILS_DELIVERY_HOUR);
						}
				);
	}

	@Test
	void givenAnonOrderPostApiCall_whenInvalidPaymentType_thenThrowException() throws Exception {
		// Act

		// post api call to create anon order
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
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("orderDetails.paymentMethod");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.ORDER_DETAILS_PAYMENT);
						}
				);
	}

	@Test
	void givenAnonOrderPostApiCall_whenInvalidChangeRequest_thenThrowException() throws Exception {
		// Act

		// post api call to create anon order
		MockHttpServletResponse response = mockMvc.perform(post("/api/anon/order")
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
				.andReturn()
				.getResponse();


		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getContentAsString()).isEqualTo(ValidationResponses.ORDER_DETAILS_CHANGE_REQUESTED);
	}

	@Test
	void givenAnonOrderPostApiCall_whenInvalidComment_thenThrowException() throws Exception {
		// Act

		// post api call to create anon order
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
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("orderDetails.comment");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.ORDER_DETAILS_COMMENT);
						}
				);
	}

	@Test
	void givenAnonOrderPostApiCall_whenCartIsEmpty_thenThrowException() throws Exception {
		// Act

		// post api call to create anon order
		MockHttpServletResponse response = mockMvc.perform(post("/api/anon/order")
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
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getContentAsString()).isEqualTo(ValidationResponses.CART_IS_EMPTY);
	}

	NewAnonOrderDTO anonOrderStub(String customerName, int customerNumber, String customerEmail, String street,
								  int streetNumber, String floor, String door, Double changeRequested,
								  String deliveryHour, String paymentType, String comment,
								  boolean emptyCart) {
		Cart cartStub = new Cart.Builder()
				.withCartItems(List.of(new CartItem.Builder()
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