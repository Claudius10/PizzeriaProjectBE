package PizzaApp.api.security;

import PizzaApp.api.entity.dto.misc.RegisterDTO;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.logging.Logger;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegisterTests {

	private final Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void givenExistingEmail_whenRegister_thenThrowException() throws Exception {
		logger.info("Register test: try to register with existing email");

		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new RegisterDTO("Clau",
										"clau@gmail.com",
										"clau@gmail.com",
										"password",
										"password")))
						.with(csrf()))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(DataIntegrityViolationException.class)));

		logger.info("Register test: successfully NOT registered already exiting email");
	}

	@Test
	public void givenInvalidName_whenRegister_thenThrowException() throws Exception {
		logger.info("Register test: try to register with invalid username");

		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new RegisterDTO("Clau$·%$$·T43",
										"clau@gmail.com",
										"clau@gmail.com",
										"password",
										"password")))
						.with(csrf()))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(MethodArgumentNotValidException.class)));

		logger.info("Register test: successfully NOT registered invalid username");
	}

	@Test
	public void givenInvalidEmail_whenRegister_thenThrowException() throws Exception {
		logger.info("Register test: try to register with invalid email");

		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new RegisterDTO("Clau",
										"claugmail$·/.com",
										"clau@gmail.com",
										"password",
										"password")))
						.with(csrf()))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(MethodArgumentNotValidException.class)));

		logger.info("Register test: successfully NOT registered invalid email");
	}

	@Test
	public void givenInvalidPassword_whenRegister_thenThrowException() throws Exception {
		logger.info("Register test: try to register with invalid password");

		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new RegisterDTO("Clau",
										"clau@gmail.com",
										"clau@gmail.com",
										"pass",
										"pass")))
						.with(csrf()))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(MethodArgumentNotValidException.class)));

		logger.info("Register test: successfully NOT registered invalid password");
	}

	@Test
	public void givenNonMatchingEmails_whenRegister_thenThrowException() throws Exception {
		logger.info("Register test: try to register without matching emails");

		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new RegisterDTO("Clau",
										"clau1@gmail.com",
										"clau2@gmail.com",
										"password",
										"password")))
						.with(csrf()))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(MethodArgumentNotValidException.class)));

		logger.info("Register test: successfully NOT registered non matching emails");
	}

	@Test
	public void givenNonMatchingPasswords_whenRegister_thenThrowException() throws Exception {
		logger.info("Register test: try to register without matching passwords");

		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new RegisterDTO("Clau",
										"clau@gmail.com",
										"clau@gmail.com",
										"password1",
										"password2")))
						.with(csrf()))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(MethodArgumentNotValidException.class)));

		logger.info("Register test: successfully NOT registered non matching passwords");
	}
}
