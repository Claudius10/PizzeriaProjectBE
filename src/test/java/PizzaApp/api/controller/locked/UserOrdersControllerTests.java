package PizzaApp.api.controller.locked;

import PizzaApp.api.configs.security.utils.SecurityCookieUtils;
import PizzaApp.api.configs.security.utils.SecurityTokenUtils;
import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.entity.order.dto.NewUserOrderDTO;
import PizzaApp.api.entity.role.Role;
import PizzaApp.api.repos.role.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

	@Test
	public void givenGetApiCallToFindOrder_thenReturnOrder() throws Exception {

		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create JWT token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		// create DTO object
		NewUserOrderDTO newUserOrderDTO = new NewUserOrderDTO(1L, 1L, new OrderDetails(), new Cart());

		// post api call to create user order

		// Act
		MockHttpServletResponse response = mockMvc.perform(get("/api/user/orders/{orderId}", 1)
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(1), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))
				.andReturn().getResponse();

		// get api call to find user order

		// Assert

	}

	@Test
	public void givenGetApiCallToFindOrder_whenOrderNotFound_thenReturnNotFound() throws Exception {

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
}