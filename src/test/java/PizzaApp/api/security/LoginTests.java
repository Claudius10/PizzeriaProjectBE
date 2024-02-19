package PizzaApp.api.security;

import PizzaApp.api.entity.dto.auth.RegisterDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.logging.Logger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginTests {

	private final Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeAll
	@AfterAll
	void cleanUp() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, "users_roles", "users_addresses", "user");
	}

	public void createUserTestSubject(RegisterDTO registerDTO) throws Exception {
		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerDTO))
						.with(csrf()))
				.andExpect(status().isOk());
	}

	@Test
	public void givenCorrectCredentials_whenLogin_thenLogin() throws Exception {
		logger.info("Authentication test: login with correct credentials");

		createUserTestSubject(new RegisterDTO(
				"LoginTest",
				"LoginTestWithCorrectCredentials@gmail.com",
				"LoginTestWithCorrectCredentials@gmail.com",
				"password",
				"password"));

		mockMvc.perform(post("/api/auth/login?username=LoginTestWithCorrectCredentials@gmail.com&password=password")
						.with(csrf()))
				.andExpect(status().isOk());

		logger.info("Authentication test: successfully logged in with correct credentials");
	}

	@Test
	public void givenNonExistingCredentials_whenLogin_thenReturnIsUnauthorized() throws Exception {
		logger.info("Authentication test: login with non existing credentials");

		mockMvc.perform(post("/api/auth/login?username=void@email.com&password=randomPassword")
						.with(csrf()))
				.andExpect(status().isUnauthorized());

		logger.info("Authentication test: successfully NOT logged in with non existing credentials");
	}

	@Test
	public void givenBadPassword_whenLogin_thenReturnIsUnauthorized() throws Exception {
		logger.info("Authentication test: login with wrong password to existing username");

		createUserTestSubject(new RegisterDTO(
				"LoginTest",
				"LoginTestWithWrongPasswordToExistingUser@gmail.com",
				"LoginTestWithWrongPasswordToExistingUser@gmail.com",
				"password",
				"password"));


		mockMvc.perform(post("/api/auth/login?username=LoginTestWithWrongPasswordToExistingUser@gmail.com&password=wrong_password")
						.with(csrf()))
				.andExpect(status().isUnauthorized());

		logger.info("Authentication test: successfully NOT logged in with wrong password to existing username");
	}

	@Test
	public void givenBadUsername_whenLogin_thenReturnIsUnauthorized() throws Exception {
		logger.info("Authentication test: login with wrong username to existing password");

		createUserTestSubject(new RegisterDTO(
				"LoginTest",
				"LoginTestWithWrongUserNameToExistingPassword@gmail.com",
				"LoginTestWithWrongUserNameToExistingPassword@gmail.com",
				"password",
				"password"));


		mockMvc.perform(post("/api/auth/login?username=asdsadsadsadasdsa@gmail.com&password=password")
						.with(csrf()))
				.andExpect(status().isUnauthorized());

		logger.info("Authentication test: successfully NOT logged in with wrong username to existing password");
	}
}
