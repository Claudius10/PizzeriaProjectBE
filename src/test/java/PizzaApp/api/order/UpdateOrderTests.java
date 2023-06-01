package PizzaApp.api.order;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import PizzaApp.api.entity.cart.Cart;
import PizzaApp.api.entity.clients.Address;
import PizzaApp.api.entity.clients.Email;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.OrderItem;
import PizzaApp.api.services.order.OrdersServiceImpl;
import PizzaApp.api.validation.exceptions.OrderDataUpdateTimeLimitException;
import PizzaApp.api.validation.exceptions.OrderDeleteTimeLimitException;

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
	private OrdersServiceImpl ordersService;

	// test objects
	private Address originalAddress;
	private Email originalEmail;
	private OrderDetails originalOrderDetails;
	private Cart originalCart;
	private Order order;
	private Long savedOrderId;

	@BeforeAll
	void setup() {

		// create an order which will be used to perform updating tests

		originalAddress = new Address("OriginalAddress", 12, "", "", "1", "3");
		originalEmail = new Email("originalEmail@email.com");

		originalOrderDetails = new OrderDetails();
		originalOrderDetails.setDeliveryHour("ASAP");
		originalOrderDetails.setPaymentType("Credit Card");

		// items
		List<OrderItem> orderItems = new ArrayList<>();
		OrderItem testItem = new OrderItem("pizza", "Carbonara", "Mediana", 1, 14.75);
		orderItems.add(testItem);

		// cart
		originalCart = new Cart();
		originalCart.setOrderItems(orderItems);
		originalCart.setTotalCost(14.75);
		originalCart.setTotalCostOffers(0D);
		originalCart.setTotalQuantity(1);

		// order
		order = new Order();

		order.setCustomerFirstName("OriginalCustomer");
		order.setContactTel(333666999);
		order.setEmail(originalEmail);
		order.setUser(null);

		order.setAddress(originalAddress);
		order.setOrderDetails(originalOrderDetails);
		order.setCart(originalCart);
		order.setCreatedOn(LocalDateTime.now());

		// for these tests to work, the endpoints must return the Order obj
		// not just the id
		
		savedOrderId = ordersService.createOrUpdate(order);
	}

	@Test
	@org.junit.jupiter.api.Order(1)
	@DisplayName("Update order test #1: correctly created original order")
	public void givenOrder_whenCreateOrUpdate_thenReturnOrder() throws Exception {

		// when action: get order
		ResultActions orderResponse = mockMvc
				.perform(get("/api/order/{id}/{orderContactTel}", savedOrderId, order.getContactTel()));

		// then expect/assert the returned order data matches the data set for testing
		logger.info("Update order test #1: correctly created original order");
		orderResponse.andExpect(status().isOk())
				.andExpect(jsonPath("$.customerFirstName", is(order.getCustomerFirstName())))
				.andExpect(jsonPath("$.contactTel", is(order.getContactTel())))
				.andExpect(jsonPath("$.email.email", is(order.getEmail().getEmail())))
				.andExpect(jsonPath("$.address.street", is(originalAddress.getStreet())))
				.andExpect(jsonPath("$.address.streetNr", is(originalAddress.getStreetNr())))
				.andExpect(jsonPath("$.address.gate", is(originalAddress.getGate())))
				.andExpect(jsonPath("$.address.staircase", is(originalAddress.getStaircase())))
				.andExpect(jsonPath("$.address.floor", is(originalAddress.getFloor())))
				.andExpect(jsonPath("$.address.door", is(originalAddress.getDoor())))
				.andExpect(jsonPath("$.orderDetails.deliveryHour", is(originalOrderDetails.getDeliveryHour())))
				.andExpect(jsonPath("$.orderDetails.paymentType", is(originalOrderDetails.getPaymentType())))
				.andExpect(jsonPath("$.cart.totalQuantity", is(originalCart.getTotalQuantity())))
				.andExpect(jsonPath("$.cart.totalCost", is(originalCart.getTotalCost())))
				.andExpect(jsonPath("$.cart.totalCostOffers", is(originalCart.getTotalCostOffers())));
		logger.info("Update order test #1: successfully created original order");
	}

	@Test
	@org.junit.jupiter.api.Order(2)
	@DisplayName("Update order test #2: update customer data")
	public void givenNewCustomerData_whenCreateOrUpdate_thenReturnOrderWithNewCustomerData()
			throws JsonProcessingException, Exception {

		// given / preparation:
		Order theOrder = new Order();
		theOrder.setCustomerFirstName("NewCustomer");
		theOrder.setCustomerLastName("NewCustomerTester");
		theOrder.setContactTel(123456789);
		theOrder.setEmail(originalEmail);
		theOrder.setCreatedOn(order.getCreatedOn());

		// set the order id
		theOrder.setId(savedOrderId);

		// when action: update order
		ResultActions orderResponse = mockMvc.perform(put("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(theOrder)));

		// then expect/assert the returned order data matches the data set for testing
		logger.info("Update order test #2: update customer data");
		orderResponse.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.customerFirstName", is(theOrder.getCustomerFirstName())))
				.andExpect(jsonPath("$.customerLastName", is(theOrder.getCustomerLastName())))
				.andExpect(jsonPath("$.contactTel", is(theOrder.getContactTel())))
				.andExpect(jsonPath("$.email.email", is(theOrder.getEmail().getEmail())))
				.andExpect(jsonPath("$.address").exists()).andExpect(jsonPath("$.cart").exists())
				.andExpect(jsonPath("$.cart.orderItems.length()", is(1)))
				.andExpect(jsonPath("$.orderDetails").exists());
		logger.info("Update order test #2: successfully updated customer data");
	}

	@Test
	@org.junit.jupiter.api.Order(3)
	@DisplayName("Update order test #3: update email")
	public void givenNewEmail_whenCreateOrUpdate_thenReturnOrderWithNewEmail()
			throws JsonProcessingException, Exception {

		// given / preparation:
		Order theOrder = new Order();
		theOrder.setCustomerFirstName("SecondCustomer");
		theOrder.setCustomerLastName("CustomerTwo");
		theOrder.setContactTel(123456789);
		theOrder.setEmail(new Email("NewEmail@email.com"));
		theOrder.setCreatedOn(order.getCreatedOn());

		// set the order id
		theOrder.setId(savedOrderId);

		// when action: update order
		ResultActions orderResponse = mockMvc.perform(put("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(theOrder)));

		// then expect/assert the returned order data matches the data set for testing
		logger.info("Update order test #3: update email");
		orderResponse.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.customerFirstName", is(theOrder.getCustomerFirstName())))
				.andExpect(jsonPath("$.customerLastName", is(theOrder.getCustomerLastName())))
				.andExpect(jsonPath("$.contactTel", is(theOrder.getContactTel())))
				.andExpect(jsonPath("$.email.email", is(theOrder.getEmail().getEmail())))
				.andExpect(jsonPath("$.address").exists()).andExpect(jsonPath("$.cart").exists())
				.andExpect(jsonPath("$.cart.orderItems.length()", is(1)))
				.andExpect(jsonPath("$.orderDetails").exists());
		logger.info("Update order test #3: successfully updated email");
	}

	@Test
	@org.junit.jupiter.api.Order(4)
	@DisplayName("Update order test #4: update address")
	public void givenNewAddress_whenCreateOrUpdate_thenReturnOrderWithUpdatedAddress()
			throws JsonProcessingException, Exception {

		// given / preparation:
		Address newAddress = new Address("NewAddress", 33, "", "", "9", "6");

		Order theOrder = new Order();
		theOrder.setAddress(newAddress);
		theOrder.setCreatedOn(order.getCreatedOn());

		// set the order id
		theOrder.setId(savedOrderId);

		// when action: update order
		ResultActions orderResponse = mockMvc.perform(put("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(theOrder)));

		// then expect/assert the returned order data matches the data set for testing
		logger.info("Update order test #4: update address");
		orderResponse.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.address.street", is(newAddress.getStreet())))
				.andExpect(jsonPath("$.address.streetNr", is(newAddress.getStreetNr())))
				.andExpect(jsonPath("$.address.gate", is(newAddress.getGate())))
				.andExpect(jsonPath("$.address.staircase", is(newAddress.getStaircase())))
				.andExpect(jsonPath("$.address.floor", is(newAddress.getFloor())))
				.andExpect(jsonPath("$.address.door", is(newAddress.getDoor())))
				.andExpect(jsonPath("$.customerFirstName").exists()).andExpect(jsonPath("$.customerLastName").exists())
				.andExpect(jsonPath("$.contactTel").exists()).andExpect(jsonPath("$.cart").exists())
				.andExpect(jsonPath("$.cart.orderItems.length()", is(1)))
				.andExpect(jsonPath("$.orderDetails").exists());
		logger.info("Update order test #4: successfully updated address");
	}

	@Test
	@org.junit.jupiter.api.Order(5)
	@DisplayName("Update order test #5: update orderDetails")
	public void givenOrderDetails_whenCreateOrUpdate_thenReturnOrderWithNewOrderDetails()
			throws JsonProcessingException, Exception {

		// given / preparation:
		OrderDetails newOrderDetails = new OrderDetails();
		newOrderDetails.setDeliveryHour("15:30");
		newOrderDetails.setPaymentType("Cash");
		newOrderDetails.setChangeRequested(15.00);
		newOrderDetails.setDeliveryComment("Well cut and hot");

		Order theOrder = new Order();
		theOrder.setOrderDetails(newOrderDetails);
		theOrder.setCreatedOn(order.getCreatedOn());

		// set the order id
		theOrder.setId(savedOrderId);

		// when action: update order
		ResultActions orderResponse = mockMvc.perform(put("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(theOrder)));

		// then expect/assert the returned order data matches the data set for testing
		logger.info("Update order test #5: update orderDetails");
		orderResponse.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.orderDetails.deliveryHour", is(newOrderDetails.getDeliveryHour())))
				.andExpect(jsonPath("$.orderDetails.paymentType", is(newOrderDetails.getPaymentType())))
				.andExpect(jsonPath("$.orderDetails.changeRequested", is(newOrderDetails.getChangeRequested())))
				.andExpect(jsonPath("$.orderDetails.deliveryComment", is(newOrderDetails.getDeliveryComment())))
				.andExpect(jsonPath("$.customerFirstName").exists()).andExpect(jsonPath("$.customerLastName").exists())
				.andExpect(jsonPath("$.contactTel").exists()).andExpect(jsonPath("$.address").exists())
				.andExpect(jsonPath("$.cart").exists()).andExpect(jsonPath("$.cart.orderItems.length()", is(1)))
				.andExpect(jsonPath("$.orderDetails").exists());
		logger.info("Update order test #5: successfully updated orderDetails");
	}

	@Test
	@org.junit.jupiter.api.Order(6)
	@DisplayName("Update order test #6: update cart")
	public void givenCart_whenCreateOrUpdate_thenReturnOrderWithNewCart() throws JsonProcessingException, Exception {

		// given / preparation:

		// items
		List<OrderItem> orderItems = new ArrayList<>();
		OrderItem testItem = new OrderItem("pizza", "Trufa Gourmet", "Mediana", 1, 14.75);
		OrderItem testItem2 = new OrderItem("pizza", "Carbonara", "Familiar", 1, 20.25);
		orderItems.add(testItem);
		orderItems.add(testItem2);

		// cart
		Cart newCart = new Cart();
		newCart.setOrderItems(orderItems);
		newCart.setTotalCost(35D);
		newCart.setTotalCostOffers(27.6);
		newCart.setTotalQuantity(2);

		// set new order details change request value to not trigger exception
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setDeliveryHour("15:30");
		orderDetails.setPaymentType("Cash");
		orderDetails.setChangeRequested(50D);
		orderDetails.setDeliveryComment("Well cut and hot");

		Order theOrder = new Order();
		theOrder.setCart(newCart);
		theOrder.setOrderDetails(orderDetails);
		theOrder.setCreatedOn(order.getCreatedOn());

		// set the order id
		theOrder.setId(savedOrderId);

		// when action: update order
		ResultActions orderResponse = mockMvc.perform(put("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(theOrder)));

		// then expect/assert the returned order data matches the data set for testing
		logger.info("Update order test #6: update cart");
		orderResponse.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.cart.totalCost", is(newCart.getTotalCost())))
				.andExpect(jsonPath("$.cart.totalCostOffers", is(newCart.getTotalCostOffers())))
				.andExpect(jsonPath("$.cart.totalQuantity", is(newCart.getTotalQuantity())))
				.andExpect(jsonPath("$.cart.orderItems.length()", is(2)))
				.andExpect(jsonPath("$.customerFirstName").exists()).andExpect(jsonPath("$.customerLastName").exists())
				.andExpect(jsonPath("$.contactTel").exists()).andExpect(jsonPath("$.address").exists())
				.andExpect(jsonPath("$.orderDetails").exists());
		logger.info("Update order test #6: successfully updated cart");
	}

	@Test
	@org.junit.jupiter.api.Order(7)
	@DisplayName("Update order test #7: update cart after time limit")
	public void givenCartUpdateAfterTimeLimit_whenCreateOrUpdate_thenThrowException()
			throws JsonProcessingException, Exception {

		// create order which will be used to perform test

		Order orderUpdateCartTimeLimit = new Order();
		orderUpdateCartTimeLimit.setCustomerFirstName("CartUpdateTimeLimit");
		orderUpdateCartTimeLimit.setContactTel(666999666);
		orderUpdateCartTimeLimit.setAddress(originalAddress);
		orderUpdateCartTimeLimit.setEmail(originalEmail);
		orderUpdateCartTimeLimit.setOrderDetails(originalOrderDetails);
		orderUpdateCartTimeLimit.setCart(originalCart);
		orderUpdateCartTimeLimit.setUser(null);
		// see appropiate createdOn for the test to work
		orderUpdateCartTimeLimit.setCreatedOn(LocalDateTime.now().minusMinutes(16));

		Long orderCartTimeLimitId = ordersService.createOrUpdate(orderUpdateCartTimeLimit);

		// given / preparation:

		// items
		List<OrderItem> orderItems = new ArrayList<>();
		OrderItem testItem = new OrderItem("pizza", "Roni Pepperoni", "Familiar", 1, 18.30);
		OrderItem testItem2 = new OrderItem("pizza", "Carbonara", "Familiar", 1, 20.25);
		orderItems.add(testItem);
		orderItems.add(testItem2);

		// cart
		Cart newCart = new Cart();
		newCart.setOrderItems(orderItems);
		newCart.setTotalCost(38.55);
		newCart.setTotalCostOffers(29.4);
		newCart.setTotalQuantity(2);

		// set new order details change request value to not trigger exception
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setDeliveryHour("ASAP");
		orderDetails.setPaymentType("Card");
		orderDetails.setDeliveryComment("Well cut and hot");

		Order theOrder = new Order();
		theOrder.setOrderDetails(orderDetails);
		theOrder.setCart(newCart);
		theOrder.setCreatedOn(orderUpdateCartTimeLimit.getCreatedOn());

		// set the order id
		theOrder.setId(orderCartTimeLimitId);

		// when action: update order
		ResultActions orderResponse = mockMvc.perform(put("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(theOrder)));

		// if the returned cart is orderUpdateCartTimeLimit's originalCart then the
		// newCart was set to null
		// upon validation and thereafter the original order's cart was set back
		// (orderUpdateCartTimeLimit's originalCart)
		// so cart was not updated, but orderDetails was updated since it's before the
		// 20 min time limit
		logger.info("Update order test #7: update cart after time limit");

		orderResponse.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.customerFirstName", is(orderUpdateCartTimeLimit.getCustomerFirstName())))
				.andExpect(jsonPath("$.contactTel", is(orderUpdateCartTimeLimit.getContactTel())))
				.andExpect(jsonPath("$.email.email", is(orderUpdateCartTimeLimit.getEmail().getEmail())))
				.andExpect(jsonPath("$.address.street", is(orderUpdateCartTimeLimit.getAddress().getStreet())))
				.andExpect(jsonPath("$.address.streetNr", is(orderUpdateCartTimeLimit.getAddress().getStreetNr())))
				.andExpect(jsonPath("$.address.gate", is(orderUpdateCartTimeLimit.getAddress().getGate())))
				.andExpect(jsonPath("$.address.staircase", is(orderUpdateCartTimeLimit.getAddress().getStaircase())))
				.andExpect(jsonPath("$.address.floor", is(orderUpdateCartTimeLimit.getAddress().getFloor())))
				.andExpect(jsonPath("$.address.door", is(orderUpdateCartTimeLimit.getAddress().getDoor())))
				.andExpect(jsonPath("$.orderDetails.deliveryHour", is(theOrder.getOrderDetails().getDeliveryHour())))
				.andExpect(jsonPath("$.orderDetails.paymentType", is(theOrder.getOrderDetails().getPaymentType())))
				.andExpect(jsonPath("$.cart.totalQuantity", is(orderUpdateCartTimeLimit.getCart().getTotalQuantity())))
				.andExpect(jsonPath("$.cart.totalCost", is(orderUpdateCartTimeLimit.getCart().getTotalCost())))
				.andExpect(jsonPath("$.cart.totalCostOffers",
						is(orderUpdateCartTimeLimit.getCart().getTotalCostOffers())));
		logger.info("Update order test #7: cart successfully NOT updated due to time limit constraint");
	}

	@Test
	@org.junit.jupiter.api.Order(8)
	@DisplayName("Update order test #8: update order data after time limit")
	public void givenOrderDataUpdateAfterTimeLimit_whenCreateOrUpdate_thenThrowException()
			throws JsonProcessingException, Exception {

		// order data = customer data (fName, lName, tel, email), address, orderDetails

		// create order which will be used to perform test

		Order orderUpdateDataTimeLimit = new Order();
		orderUpdateDataTimeLimit.setCustomerFirstName("OrderDataUpdateTimeLimit");
		orderUpdateDataTimeLimit.setContactTel(333999333);
		orderUpdateDataTimeLimit.setAddress(originalAddress);
		orderUpdateDataTimeLimit.setEmail(originalEmail);
		orderUpdateDataTimeLimit.setOrderDetails(originalOrderDetails);
		orderUpdateDataTimeLimit.setCart(originalCart);
		orderUpdateDataTimeLimit.setUser(null);
		// see appropiate createdOn for the test to work
		orderUpdateDataTimeLimit.setCreatedOn(LocalDateTime.now().minusMinutes(21));

		Long orderDataTimeLimitId = ordersService.createOrUpdate(orderUpdateDataTimeLimit);

		// given / preparation:

		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setDeliveryHour("22:30");
		orderDetails.setPaymentType("Tarjeta");
		orderDetails.setDeliveryComment("No llamar al timbre duerme el bebe");

		Order theOrder = new Order();
		theOrder.setCustomerFirstName("ExceptionToCatch");
		theOrder.setContactTel(123123123);
		theOrder.setOrderDetails(orderDetails);
		theOrder.setCreatedOn(orderUpdateDataTimeLimit.getCreatedOn());

		// set the order id
		theOrder.setId(orderDataTimeLimitId);

		// when action: update order
		ResultActions orderResponse = mockMvc.perform(put("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(theOrder)));

		// check exception is thrown
		logger.info("Update order test #8: update order data after time limit");

		orderResponse.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
				CoreMatchers.instanceOf(OrderDataUpdateTimeLimitException.class)));

		logger.info("Update order test #8: order data successfully NOT updated due to time limit constraint");
	}

	@Test
	@org.junit.jupiter.api.Order(9)
	@DisplayName("Update order test #9: delete order after time limit")
	public void givenOrderDeleteAfterTimeLimit_whenCreateOrUpdate_thenThrowException()
			throws JsonProcessingException, Exception {

		// create order which will be used to perform test

		Order orderDeleteTimeLimit = new Order();
		orderDeleteTimeLimit.setCustomerFirstName("OrderDeleteTimeLimit");
		orderDeleteTimeLimit.setContactTel(777333777);
		orderDeleteTimeLimit.setAddress(originalAddress);
		orderDeleteTimeLimit.setEmail(originalEmail);
		orderDeleteTimeLimit.setUser(null);
		orderDeleteTimeLimit.setOrderDetails(originalOrderDetails);
		orderDeleteTimeLimit.setCart(originalCart);
		// see appropiate createdOn for the test to work
		orderDeleteTimeLimit.setCreatedOn(LocalDateTime.now().minusMinutes(36));

		Long orderDeleteTimeLimitId = ordersService.createOrUpdate(orderDeleteTimeLimit);

		// when action: delete order
		ResultActions orderResponse = mockMvc.perform(delete("/api/order/{id}", orderDeleteTimeLimitId));

		// check exception is thrown
		logger.info("Update order test #9: delete order after time limit");

		orderResponse.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
				CoreMatchers.instanceOf(OrderDeleteTimeLimitException.class)));

		logger.info("Update order test #9: order successfully NOT deleted due to time limit constraint");
	}
}