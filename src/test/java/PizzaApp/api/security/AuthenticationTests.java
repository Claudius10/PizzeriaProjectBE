package PizzaApp.api.security;

import PizzaApp.api.entity.user.dto.LoginDTO;
import PizzaApp.api.entity.user.dto.RegisterDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.logging.Logger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationTests {

	private final Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void givenNonExistingCredentials_whenLogin_thenThrowException() throws Exception {
		logger.info("Authentication test #1: login with non existing credentials");

		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new LoginDTO("void@email.com", "randomPassword"))))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(BadCredentialsException.class)));

		logger.info("Authentication test #1: successfully NOT logged in with non existing credentials");
	}

	@Test
	public void givenBadPassword_whenLogin_thenThrowException() throws Exception {
		logger.info("Authentication test #2: login with wrong password to existing username");

		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new LoginDTO("clau@gmail.com", "wrong-password"))))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(BadCredentialsException.class)));

		logger.info("Authentication test #2: successfully NOT logged in with wrong password to existing username");
	}

	@Test
	public void givenCorrectCredentials_whenLogin_thenThrowException() throws Exception {
		logger.info("Authentication test #3: login with correct credentialing");

		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new LoginDTO("clau@gmail.com", "password"))))
				.andExpect(status().isOk());

		logger.info("Authentication test #3: successfully logged in with correct credentials");
	}

	@Test
	public void givenExistingUserName_whenRegister_thenThrowException() throws Exception {
		logger.info("Authentication test #4: try to register with existing username");

		mockMvc.perform(post("/api/auth/account/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new RegisterDTO.Builder()
										.withEmail("clau@gmail.com")
										.withMatchingEmail("clau@gmail.com")
										.withPassword("password")
										.withMatchingPassword("password")
										.build())))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(DataIntegrityViolationException.class)));

		logger.info("Authentication test #4: successfully NOT registered already exiting username");
	}
}
