package PizzaApp.api.order;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
import PizzaApp.api.entity.clients.customer.Customer;
import PizzaApp.api.entity.clients.customer.Telephone;
import PizzaApp.api.entity.order.Order;
import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.OrderItem;
import PizzaApp.api.exceptions.ChangeRequestedNotValidException;
import PizzaApp.api.exceptions.EmptyCartException;
import PizzaApp.api.services.order.OrdersService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class UpdateOrderTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OrdersService ordersService;

	// test objects
	private Address originalAddress;
	private Telephone originalTel;
	private Customer originalCustomer;
	private OrderDetails originalOrderDetails;
	private Cart originalCart;
	private Long savedOrderId;

	@BeforeAll
	void setup() {

		// create an order which will be used to perform updating tests

		originalAddress = new Address("originalAddress", 5, "", "", "15", "5");
		originalTel = new Telephone(666333999);
		originalCustomer = new Customer("originalCustomer", "", "originalCust@email.com", originalTel);
		originalOrderDetails = new OrderDetails();
		originalOrderDetails
				.setOrderDate(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH:mm:ss")));
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
		originalCart.setTotalCostOffers(0);
		originalCart.setTotalQuantity(1);

		// order
		Order order = new Order();
		order.setId((long) 0);
		order.setAddress(originalAddress);
		order.setCustomer(originalCustomer);
		order.setOrderDetails(originalOrderDetails);
		order.setCart(originalCart);

		ordersService.createOrUpdate(order);

		savedOrderId = order.getId();
	}

	@Test
	@org.junit.jupiter.api.Order(1)
	@DisplayName("Test for checking setup order was corretly created")
	public void givenOrder_whenCreateOrUpdate_thenReturnOrder() throws Exception {

		// when action
		// get order
		ResultActions orderResponse = mockMvc.perform(get("/api/order/{id}", savedOrderId));

		// then expect/assert
		// the returned order data matches the data set for testing
		System.out.println("-------- Create Order for Tests --------");
		orderResponse.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.customer.firstName", is(originalCustomer.getFirstName())))
				.andExpect(jsonPath("$.customer.lastName", is(originalCustomer.getLastName())))
				.andExpect(jsonPath("$.customer.tel.number", is(originalTel.getNumber())))
				.andExpect(jsonPath("$.customer.email", is(originalCustomer.getEmail())))
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
	}

	@Test
	@org.junit.jupiter.api.Order(2)
	@DisplayName("Test for updating customer tel")
	public void givenNewCustomerTel_whenCreateOrUpdate_thenReturnOrderWithNewCustomerTel()
			throws JsonProcessingException, Exception {

		// given / preparation:
		Telephone newTel = new Telephone(666333666);
		Customer originalCustomerNewTel = new Customer("originalCustomer", "", "originalCust@email.com", newTel);

		// set customer with new tel
		Order order = new Order();
		order.setCustomer(originalCustomerNewTel);

		// set the order id
		order.setId(savedOrderId);

		// when action:

		// update order
		ResultActions orderResponse = mockMvc.perform(put("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert

		System.out.println("-------- Updating Customer --------");
		orderResponse.andDo(print()).andExpect(status().isAccepted())
				.andExpect(jsonPath("$.customer.firstName", is(originalCustomerNewTel.getFirstName())))
				.andExpect(jsonPath("$.customer.lastName", is(originalCustomerNewTel.getLastName())))
				.andExpect(jsonPath("$.customer.tel.number", is(originalCustomerNewTel.getTel().getNumber())))
				.andExpect(jsonPath("$.customer.email", is(originalCustomerNewTel.getEmail())))
				.andExpect(jsonPath("$.address").exists()).andExpect(jsonPath("$.cart").exists())
				.andExpect(jsonPath("$.orderDetails").exists());
		System.out.println("-------- Customer update: returned customer matches customer set for updating --------");
	}

	@Test
	@org.junit.jupiter.api.Order(3)
	@DisplayName("Test for updating customer data")
	public void givenNewCustomer_whenCreateOrUpdate_thenReturnOrderWithNewCustomer()
			throws JsonProcessingException, Exception {

		// given / preparation:
		Customer newCustomerOriginalTel = new Customer("newCustomerOriginalTel", "NewCustomer",
				"newCustomerOrigTel@email.com", originalTel);

		// set customer with new tel
		Order order = new Order();
		order.setCustomer(newCustomerOriginalTel);

		// set the order id
		order.setId(savedOrderId);

		// when action:

		// update order
		ResultActions orderResponse = mockMvc.perform(put("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert

		System.out.println("-------- Updating Customer --------");
		orderResponse.andDo(print()).andExpect(status().isAccepted())
				.andExpect(jsonPath("$.customer.firstName", is(newCustomerOriginalTel.getFirstName())))
				.andExpect(jsonPath("$.customer.lastName", is(newCustomerOriginalTel.getLastName())))
				.andExpect(jsonPath("$.customer.tel.number", is(newCustomerOriginalTel.getTel().getNumber())))
				.andExpect(jsonPath("$.customer.email", is(newCustomerOriginalTel.getEmail())))
				.andExpect(jsonPath("$.address").exists()).andExpect(jsonPath("$.cart").exists())
				.andExpect(jsonPath("$.orderDetails").exists());
		System.out.println("-------- Customer update: returned customer matches customer set for updating --------");
	}

	@Test
	@org.junit.jupiter.api.Order(4)
	@DisplayName("Test for updating customer and tel")
	public void givenNewCustomerAndNewTel_whenCreateOrUpdate_thenReturnOrderWithNewCustomerAndNewTel()
			throws JsonProcessingException, Exception {

		// given / preparation:
		Telephone secondNewTel = new Telephone(666999333);
		Customer newCustomerNewTel = new Customer("newCustomerNewTel", "AllNew", "newCustomerNewTel@email.com",
				secondNewTel);

		// set customer with new tel
		Order order = new Order();
		order.setCustomer(newCustomerNewTel);

		// set the order id
		order.setId(savedOrderId);

		// when action:

		// update order
		ResultActions orderResponse = mockMvc.perform(put("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert

		System.out.println("-------- Updating Customer --------");
		orderResponse.andDo(print()).andExpect(status().isAccepted())
				.andExpect(jsonPath("$.customer.firstName", is(newCustomerNewTel.getFirstName())))
				.andExpect(jsonPath("$.customer.lastName", is(newCustomerNewTel.getLastName())))
				.andExpect(jsonPath("$.customer.tel.number", is(newCustomerNewTel.getTel().getNumber())))
				.andExpect(jsonPath("$.customer.email", is(newCustomerNewTel.getEmail())))
				.andExpect(jsonPath("$.address").exists()).andExpect(jsonPath("$.cart").exists())
				.andExpect(jsonPath("$.orderDetails").exists());
		System.out.println("-------- Customer update: returned customer matches customer set for updating --------");
	}

	@Test
	@org.junit.jupiter.api.Order(5)
	@DisplayName("Test for updating address")
	public void givenNewAddress_whenCreateOrUpdate_thenReturnOrderWithUpdatedAddress()
			throws JsonProcessingException, Exception {

		// given / preparation:
		Address newAddress = new Address("newAddress", 33, "", "", "9", "6");

		// set a new address for updating order
		Order order = new Order();
		order.setAddress(newAddress);

		// set the order id
		order.setId(savedOrderId);

		// when action:

		// update order
		ResultActions orderResponse = mockMvc.perform(put("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert

		System.out.println("-------- Updating Address  --------");
		orderResponse.andDo(print()).andExpect(status().isAccepted())
				.andExpect(jsonPath("$.address.street", is(newAddress.getStreet())))
				.andExpect(jsonPath("$.address.streetNr", is(newAddress.getStreetNr())))
				.andExpect(jsonPath("$.address.gate", is(newAddress.getGate())))
				.andExpect(jsonPath("$.address.staircase", is(newAddress.getStaircase())))
				.andExpect(jsonPath("$.address.floor", is(newAddress.getFloor())))
				.andExpect(jsonPath("$.address.door", is(newAddress.getDoor())))
				.andExpect(jsonPath("$.storePickUpName").doesNotExist()).andExpect(jsonPath("$.customer").exists())
				.andExpect(jsonPath("$.cart").exists()).andExpect(jsonPath("$.orderDetails").exists());

		System.out.println("-------- Address update: returned address matches address set for updating --------");
	}

	@Test
	@org.junit.jupiter.api.Order(6)
	@DisplayName("Test for updating from (home) address to store pick-up")
	public void givenStorePickUpName_whenCreateOrUpdate_thenReturnOrderWithStoreAndNotAddress()
			throws JsonProcessingException, Exception {

		// given / preparation:

		// set a store pick-up name for updating order
		Order order = new Order();
		order.setStorePickUpName("testStorePickUpName");

		// set the order id
		order.setId(savedOrderId);

		// when action:

		// update order
		ResultActions orderResponse = mockMvc.perform(put("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert

		System.out.println("-------- Updating Address to Pick-up --------");
		orderResponse.andDo(print()).andExpect(status().isAccepted())
				.andExpect(jsonPath("$.storePickUpName", is("testStorePickUpName")))
				.andExpect(jsonPath("$.address").doesNotExist()).andExpect(jsonPath("$.customer").exists())
				.andExpect(jsonPath("$.cart").exists()).andExpect(jsonPath("$.orderDetails").exists());

		System.out.println(
				"-------- Updating Address to Pick-up: returned store pick-up name matches the one set for update and address does not exist --------");

	}

	@Test
	@DisplayName("Test for updating from store pick-up to (home) address")
	@org.junit.jupiter.api.Order(7)
	public void givenAddress_whenCreateOrUpdate_thenReturnOrderWithAddressNotStore()
			throws JsonProcessingException, Exception {

		// given / preparation:

		// set a store pick-up name for updating order
		Order order = new Order();
		order.setAddress(originalAddress);

		// set the order id
		order.setId(savedOrderId);

		// when action:

		// update order
		ResultActions orderResponse = mockMvc.perform(put("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert

		System.out.println("-------- Updating Pick-up to Address --------");
		orderResponse.andDo(print()).andExpect(status().isAccepted())
				.andExpect(jsonPath("$.address.street", is(originalAddress.getStreet())))
				.andExpect(jsonPath("$.address.streetNr", is(originalAddress.getStreetNr())))
				.andExpect(jsonPath("$.address.gate", is(originalAddress.getGate())))
				.andExpect(jsonPath("$.address.staircase", is(originalAddress.getStaircase())))
				.andExpect(jsonPath("$.address.floor", is(originalAddress.getFloor())))
				.andExpect(jsonPath("$.address.door", is(originalAddress.getDoor())))
				.andExpect(jsonPath("$.storePickUpName").doesNotExist()).andExpect(jsonPath("$.customer").exists())
				.andExpect(jsonPath("$.cart").exists()).andExpect(jsonPath("$.orderDetails").exists());

		System.out.println(
				"-------- Address update: : returned order's address matches the one set for update and store pick-up name does not exist --------");

	}

	@Test
	@org.junit.jupiter.api.Order(8)
	@DisplayName("Test for updating order details")
	public void givenOrderDetails_whenCreateOrUpdate_thenReturnOrderWithNewOrderDetails()
			throws JsonProcessingException, Exception {

		// given / preparation:
		OrderDetails newOrderDetails = new OrderDetails();
		newOrderDetails.setOrderDate(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH:mm:ss")));
		newOrderDetails.setDeliveryHour("15:30");
		newOrderDetails.setPaymentType("Cash");
		newOrderDetails.setChangeRequested(15.00);
		newOrderDetails.setDeliveryComment("Well cut and hot");

		Order order = new Order();
		order.setOrderDetails(newOrderDetails);

		// set the order id
		order.setId(savedOrderId);

		// when action:

		// update order
		ResultActions orderResponse = mockMvc.perform(put("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert

		System.out.println("-------- Updating order details --------");
		orderResponse.andDo(print()).andExpect(status().isAccepted())
				.andExpect(jsonPath("$.orderDetails.deliveryHour", is(newOrderDetails.getDeliveryHour())))
				.andExpect(jsonPath("$.orderDetails.paymentType", is(newOrderDetails.getPaymentType())))
				.andExpect(jsonPath("$.orderDetails.changeRequested", is(newOrderDetails.getChangeRequested())))
				.andExpect(jsonPath("$.orderDetails.deliveryComment", is(newOrderDetails.getDeliveryComment())))
				.andExpect(jsonPath("$.storePickUpName").doesNotExist()).andExpect(jsonPath("$.customer").exists())
				.andExpect(jsonPath("$.cart").exists()).andExpect(jsonPath("$.address").exists());
		System.out.println(
				"-------- Order details updated: returned order details match the one set for update --------");
	}

	@Test
	@org.junit.jupiter.api.Order(9)
	@DisplayName("Test for updating order details with invalid change request")
	public void givenInvalidChangeRequest_whenCreateOrUpdate_thenReturnException()
			throws JsonProcessingException, Exception {

		// given / preparation:
		OrderDetails invalidOrderDetails = new OrderDetails();
		invalidOrderDetails
				.setOrderDate(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH:mm:ss")));
		invalidOrderDetails.setDeliveryHour("15:30");
		invalidOrderDetails.setPaymentType("Cash");
		invalidOrderDetails.setChangeRequested(10.00);
		invalidOrderDetails.setDeliveryComment("Well cut and hot");

		Order order = new Order();
		order.setOrderDetails(invalidOrderDetails);

		// set the order id
		order.setId(savedOrderId);

		// when action:

		// update order
		ResultActions orderResponse = mockMvc.perform(put("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert

		System.out.println("-------- Updating order details with invalid change request --------");
		orderResponse.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
				CoreMatchers.instanceOf(ChangeRequestedNotValidException.class))).andDo(print());
		System.out.println(
				"-------- Order details updated: update did not go through because change requested is invalid --------");
	}

	@Test
	@org.junit.jupiter.api.Order(10)
	@DisplayName("Test for updating cart")
	public void givenCart_whenCreateOrUpdate_thenReturnOrderWithNewCart() throws JsonProcessingException, Exception {

		// given / preparation:

		// items
		List<OrderItem> orderItems = new ArrayList<>();
		OrderItem testItem = new OrderItem("pizza", "Trufa Gourmet", "Mediana", 1, 14.75);
		OrderItem testItem2 = new OrderItem("pizza", "Carbonara", "Mediana", 1, 14.75);
		orderItems.add(testItem);
		orderItems.add(testItem2);

		// cart
		Cart newCart = new Cart();
		newCart.setOrderItems(orderItems);
		newCart.setTotalCost(29.5);
		newCart.setTotalCostOffers(23.13);
		newCart.setTotalQuantity(2);

		// set new order details change request value to not trigger exception
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setOrderDate(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH:mm:ss")));
		orderDetails.setDeliveryHour("15:30");
		orderDetails.setPaymentType("Cash");
		orderDetails.setChangeRequested(30.00);
		orderDetails.setDeliveryComment("Well cut and hot");

		Order order = new Order();
		order.setCart(newCart);
		order.setOrderDetails(orderDetails);

		// set the order id
		order.setId(savedOrderId);

		// when action:

		// update order
		ResultActions orderResponse = mockMvc.perform(put("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert

		System.out.println("-------- Updating cart --------");
		orderResponse.andDo(print()).andExpect(status().isAccepted())
				.andExpect(jsonPath("$.cart.totalCost", is(newCart.getTotalCost())))
				.andExpect(jsonPath("$.cart.totalCostOffers", is(newCart.getTotalCostOffers())))
				.andExpect(jsonPath("$.cart.totalQuantity", is(newCart.getTotalQuantity())))
				.andExpect(jsonPath("$.cart.orderItems.length()", is(2))).andExpect(jsonPath("$.customer").exists())
				.andExpect(jsonPath("$.orderDetails").exists()).andExpect(jsonPath("$.address").exists());

		System.out.println("-------- Cart updated: cart returned matches the one set for updating --------");
	}

	@Test
	@org.junit.jupiter.api.Order(11)
	@DisplayName("Test for updating order with empty cart")
	public void givenEmptyCart_whenCreateOrUpdate_thenReturnException() throws JsonProcessingException, Exception {

		// given / preparation:

		// items
		List<OrderItem> orderItems = new ArrayList<>();

		// cart
		Cart newCart = new Cart();
		newCart.setOrderItems(orderItems);
		newCart.setTotalCost((double) 0);
		newCart.setTotalCostOffers(0);
		newCart.setTotalQuantity(0);

		Order order = new Order();
		order.setCart(newCart);

		// set the order id
		order.setId(savedOrderId);

		// when action:

		// update order
		ResultActions orderResponse = mockMvc.perform(put("/api/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)));

		// then expect/assert

		System.out.println("-------- Updating order with empty cart --------");
		orderResponse.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
				CoreMatchers.instanceOf(EmptyCartException.class))).andDo(print());
		System.out.println("-------- Order cart update: update did not go through because cart is empty --------");
	}
}
