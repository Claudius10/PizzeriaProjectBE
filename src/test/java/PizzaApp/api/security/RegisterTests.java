package PizzaApp.api.security;

import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.dto.error.ApiErrorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
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
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegisterTests {

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
	public void givenRegistrationRequest_whenRegister_thenRegisterUser() throws Exception {
		logger.info("Register test: register user");

		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new RegisterDTO("Clau Dos",
										"clau2@gmail.com",
										"clau2@gmail.com",
										"password",
										"password")))
						.with(csrf()))
				.andExpect(status().isOk());

		logger.info("Register test: successfully registered user");
	}

	@Test
	public void givenExistingEmail_whenRegister_thenDontAllowRegister() throws Exception {
		logger.info("Register test: try to register with existing email");

		createUserTestSubject(new RegisterDTO(
				"RegisterTest",
				"registerAnExistingUser@gmail.com",
				"registerAnExistingUser@gmail.com",
				"password",
				"password"));

		String response = mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new RegisterDTO("Clau",
										"registerAnExistingUser@gmail.com",
										"registerAnExistingUser@gmail.com",
										"password",
										"password")))
						.with(csrf()))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

		ApiErrorDTO error = objectMapper.readValue(response, ApiErrorDTO.class);

		assertEquals("Una cuenta ya existe con el correo electrónico introducido. Si no recuerda la contraseña, contacte con nosotros", error.errorMsg());

		logger.info("Register test: successfully NOT registered already exiting email");
	}

	@Test
	public void givenInvalidName_whenRegister_thenThrowException() throws Exception {
		logger.info("Register test: try to register with invalid username");

		mockMvc.perform(post("/api/anon/register")
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

		mockMvc.perform(post("/api/anon/register")
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

		mockMvc.perform(post("/api/anon/register")
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

		mockMvc.perform(post("/api/anon/register")
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

		mockMvc.perform(post("/api/anon/register")
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
