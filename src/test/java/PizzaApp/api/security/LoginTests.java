package PizzaApp.api.security;

import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.role.Role;
import PizzaApp.api.repos.role.RoleRepository;
import PizzaApp.api.services.user.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class LoginTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@BeforeAll
	void setUp() {
		roleRepository.save(new Role("USER"));
		userRepository.create(new RegisterDTO(
				"tester",
				"test@gmail.com",
				"test@gmail.com",
				"password",
				"password"));
	}

	@Test
	public void givenLoginApiCall_whenValidCredentials_thenReturnOk() throws Exception {
		mockMvc.perform(post("/api/auth/login?username=test@gmail.com&password=password")
						.with(csrf()))
				.andExpect(status().isOk());
	}

	@Test
	public void givenLoginApiCall_whenInvalidCredentials_thenReturnUnauthorized() throws Exception {
		mockMvc.perform(post("/api/auth/login?username=void@email.com&password=randomPassword")
						.with(csrf()))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void givenLoginApiCall_whenInvalidPassword_thenReturnUnauthorized() throws Exception {
		mockMvc.perform(post("/api/auth/login?username=test@gmail.com&password=wrong_password")
						.with(csrf()))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void givenLoginApiCall_whenInvalidUsername_thenReturnUnauthorized() throws Exception {
		mockMvc.perform(post("/api/auth/login?username=nottest@gmail.com&password=password")
						.with(csrf()))
				.andExpect(status().isUnauthorized());
	}
}