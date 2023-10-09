package PizzaApp.api.security;

import PizzaApp.api.entity.dto.misc.LoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
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

	@Test
	public void givenNonExistingCredentials_whenLogin_thenThrowException() throws Exception {
		logger.info("Authentication test: login with non existing credentials");

		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new LoginDTO("void@email.com", "randomPassword")))
						.with(csrf()))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(BadCredentialsException.class)));

		logger.info("Authentication test: successfully NOT logged in with non existing credentials");
	}

	@Test
	public void givenBadPassword_whenLogin_thenThrowException() throws Exception {
		logger.info("Authentication test: login with wrong password to existing username");

		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new LoginDTO("clau@gmail.com", "wrong-password")))
						.with(csrf()))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(BadCredentialsException.class)));

		logger.info("Authentication test: successfully NOT logged in with wrong password to existing username");
	}

	@Test
	public void givenBadUsername_whenLogin_thenThrowException() throws Exception {
		logger.info("Authentication test: login with wrong username to existing password");

		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new LoginDTO("clau2@gmail.com", "password")))
						.with(csrf()))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(BadCredentialsException.class)));

		logger.info("Authentication test: successfully NOT logged in with wrong username to existing password");
	}

	@Test
	public void givenCorrectCredentials_whenLogin_thenThrowException() throws Exception {
		logger.info("Authentication test: login with correct credentials");

		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new LoginDTO("clau@gmail.com", "password")))
						.with(csrf()))
				.andExpect(status().isOk());

		logger.info("Authentication test: successfully logged in with correct credentials");
	}
}
