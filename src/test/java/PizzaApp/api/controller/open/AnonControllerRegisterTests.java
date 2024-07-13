package PizzaApp.api.controller.open;

import PizzaApp.api.entity.dto.auth.RegisterDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

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
@DirtiesContext
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

		// post api call to register new user in database
		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new RegisterDTO("Clau",
										"clau2@gmail.com",
										"clau2@gmail.com",
										"Password1",
										"Password1")))
						.with(csrf()))
				.andExpect(status().isCreated());

		// Assert

		long count = userRepository.count();
		assertThat(count).isEqualTo(1);
	}

	@Test
	public void givenRegisterApiCall_whenAccountWithEmailAlreadyExists_thenDontAllowRegister() throws Exception {
		// Arrange

		// post api call to register new user in database
		createUserTestSubject(new RegisterDTO(
				"RegisterTest",
				"registerAnExistingUser@gmail.com",
				"registerAnExistingUser@gmail.com",
				"Password1",
				"Password1"));

		// Act

		// post api call to register new user in database
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


		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getContentAsString()).isEqualTo(ValidationResponses.EMAIL_ALREADY_EXISTS);
	}

	@Test
	public void givenRegisterPostApiCall_whenInvalidUserName_thenThrowException() throws Exception {
		// Act

		// post api call to register new user in database
		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new RegisterDTO(
								"UserToRegi·%$ster",
								"emailRegister@gmail.com",
								"emailRegister@gmail.com",
								"Password1",
								"Password1")
						)))
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("name");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.NAME_INVALID);
						}
				);
	}

	@Test
	public void givenRegisterPostApiCall_whenNonMatchingEmail_thenThrowException() throws Exception {
		// Act

		// post api call to register new user in database
		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new RegisterDTO(
								"UserToRegister",
								"emailRegiste2@gmail.com",
								"emailRegister@gmail.com",
								"Password1",
								"Password1")

						)))
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("email");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.EMAIL_NO_MATCH);
						}
				);
	}

	@Test
	public void givenRegisterPostApiCall_whenInvalidEmail_thenThrowException() throws Exception {
		// Act

		// post api call to register new user in database
		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new RegisterDTO(
								"UserToRegister",
								"emailRegister$·%·$$gmail.com",
								"emailRegister$·%·$$gmail.com",
								"Password1",
								"Password1")
						)))
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("email");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.EMAIL_INVALID);
						}
				);
	}

	@Test
	public void givenRegisterPostApiCall_whenNonMatchingPassword_thenThrowException() throws Exception {
		// Act

		// post api call to register new user in database
		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new RegisterDTO(
								"UserToRegister",
								"emailRegister@gmail.com",
								"emailRegister@gmail.com",
								"Password1",
								"Password12")
						)))
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("password");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.PASSWORD_NO_MATCH);
						}
				);
	}

	@Test
	public void givenRegisterPostApiCall_whenInvalidPassword_thenThrowException() throws Exception {
		// Act

		// post api call to register new user in database
		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new RegisterDTO(
								"UserToRegister",
								"emailRegister@gmail.com",
								"emailRegister@gmail.com",
								"Password",
								"Password")
						)))
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("password");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.PASSWORD_INVALID);
						}
				);
	}

	public void createUserTestSubject(RegisterDTO registerDTO) throws Exception {
		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerDTO))
						.with(csrf()))
				.andExpect(status().isCreated());
	}
}