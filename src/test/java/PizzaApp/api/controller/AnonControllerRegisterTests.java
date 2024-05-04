package PizzaApp.api.controller;

import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.dto.error.ApiErrorDTO;
import PizzaApp.api.entity.role.Role;
import PizzaApp.api.repos.role.RoleRepository;
import PizzaApp.api.repos.user.UserRepository;
import PizzaApp.api.utils.globals.ValidationResponses;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class AnonControllerRegisterTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@BeforeAll
	public void setUp() {
		roleRepository.save(new Role("USER"));
	}

	@Test
	@Order(1)
	public void givenRegisterApiCall_thenRegisterUser() throws Exception {
		// Act

		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new RegisterDTO("Clau",
										"clau2@gmail.com",
										"clau2@gmail.com",
										"Password1",
										"Password1")))
						.with(csrf()))
				.andExpect(status().isOk());

		// Assert

		long count = userRepository.count();
		assertThat(count).isEqualTo(1);
	}

	@Test
	public void givenRegisterApiCall_whenAccountWithEmailAlreadyExists_thenDontAllowRegister() throws Exception {
		// Arrange

		createUserTestSubject(new RegisterDTO(
				"RegisterTest",
				"registerAnExistingUser@gmail.com",
				"registerAnExistingUser@gmail.com",
				"Password1",
				"Password1"));

		// Act

		MockHttpServletResponse response = mockMvc.perform(post("/api/anon/register").with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new RegisterDTO("Clau",
										"registerAnExistingUser@gmail.com",
										"registerAnExistingUser@gmail.com",
										"Password1",
										"Password1"))))
				.andReturn().getResponse();

		// Assert

		ApiErrorDTO error = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ApiErrorDTO.class);
		assertThat(error.statusCode()).isEqualTo(400);
		assertThat(error.errorMsg()).isEqualTo(ValidationResponses.ACCOUNT_WITH_EMAIL_EXISTS);
	}

	@Test
	public void givenRegisterPostApiCall_whenInvalidUserName_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new RegisterDTO(
								"UserToRegi·%$ster",
								"emailRegister@gmail.com",
								"emailRegister@gmail.com",
								"Password1",
								"Password1")
						)))
				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(400);
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("name");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.USER_NAME);
						}
				);
	}

	@Test
	public void givenRegisterPostApiCall_whenNonMatchingEmail_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new RegisterDTO(
								"UserToRegister",
								"emailRegiste2@gmail.com",
								"emailRegister@gmail.com",
								"Password1",
								"Password1")

						)))
				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(400);
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("email");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.USER_EMAIL_MATCHING);
						}
				);
	}

	@Test
	public void givenRegisterPostApiCall_whenInvalidEmail_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new RegisterDTO(
								"UserToRegister",
								"emailRegister$·%·$$gmail.com",
								"emailRegister$·%·$$gmail.com",
								"Password1",
								"Password1")
						)))
				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(400);
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("email");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.USER_EMAIL);
						}
				);
	}

	@Test
	public void givenRegisterPostApiCall_whenNonMatchingPassword_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new RegisterDTO(
								"UserToRegister",
								"emailRegister@gmail.com",
								"emailRegister@gmail.com",
								"Password1",
								"Password12")
						)))
				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(400);
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("password");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.USER_PASSWORD_MATCHING);
						}
				);
	}

	@Test
	public void givenRegisterPostApiCall_whenInvalidPassword_thenThrowException() throws Exception {
		// Assert

		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new RegisterDTO(
								"UserToRegister",
								"emailRegister@gmail.com",
								"emailRegister@gmail.com",
								"Password",
								"Password")
						)))
				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(400);
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("password");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.USER_PASSWORD);
						}
				);
	}

	public void createUserTestSubject(RegisterDTO registerDTO) throws Exception {
		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerDTO))
						.with(csrf()))
				.andExpect(status().isOk());
	}
}