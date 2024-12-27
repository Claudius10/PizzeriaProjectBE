package org.pizzeria.api.controller.open;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.pizzeria.api.repos.user.UserRepository;
import org.pizzeria.api.web.dto.api.Response;
import org.pizzeria.api.web.dto.auth.RegisterDTO;
import org.pizzeria.api.web.constants.ApiResponses;
import org.pizzeria.api.web.constants.ApiRoutes;
import org.pizzeria.api.web.constants.ValidationResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Sql(scripts = "file:src/test/resources/db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
@Sql(scripts = "file:src/test/resources/db/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = ISOLATED))
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@DirtiesContext
class AnonControllerRegisterTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Test
	@Order(1)
	void givenRegisterApiCall_thenRegisterUser() throws Exception {
		// Act

		// post api call to register new user in database
		mockMvc.perform(post(ApiRoutes.BASE + ApiRoutes.V1 + ApiRoutes.ANON_BASE + ApiRoutes.ANON_REGISTER)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new RegisterDTO("Clau",
										"clau2@gmail.com",
										"clau2@gmail.com",
										123456789,
										"Password1",
										"Password1"))))
				.andExpect(status().isCreated());

		// Assert

		long count = userRepository.count();
		assertThat(count).isEqualTo(1);
	}

	@Test
	void givenRegisterApiCall_whenAccountWithEmailAlreadyExists_thenDontAllowRegister() throws Exception {
		// Arrange

		// post api call to register new user in database
		createUserTestSubject(new RegisterDTO(
				"RegisterTest",
				"registerAnExistingUser@gmail.com",
				"registerAnExistingUser@gmail.com",
				123456789,
				"Password1",
				"Password1"));

		// Act

		// post api call to register new user in database
		MockHttpServletResponse response = mockMvc.perform(post(ApiRoutes.BASE + ApiRoutes.V1 + ApiRoutes.ANON_BASE + ApiRoutes.ANON_REGISTER)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								new RegisterDTO("Clau",
										"registerAnExistingUser@gmail.com",
										"registerAnExistingUser@gmail.com",
										123456789,
										"Password1",
										"Password1"))))
				.andReturn().getResponse();

		// Assert


		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

		Response responseObj = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Response.class);

		assertThat(responseObj.getError().getCause()).isEqualTo(ApiResponses.USER_EMAIL_ALREADY_EXISTS);
		assertThat(responseObj.getStatus().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void givenRegisterPostApiCall_whenInvalidUserName_thenThrowException() throws Exception {
		// Act

		// post api call to register new user in database
		mockMvc.perform(post(ApiRoutes.BASE + ApiRoutes.V1 + ApiRoutes.ANON_BASE + ApiRoutes.ANON_REGISTER)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new RegisterDTO(
								"UserToRegi·%$ster",
								"emailRegister@gmail.com",
								"emailRegister@gmail.com",
								123456789,
								"Password1",
								"Password1")
						)))
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("name");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.NAME_INVALID);
						}
				);
	}

	@Test
	void givenRegisterPostApiCall_whenNonMatchingEmail_thenThrowException() throws Exception {
		// Act

		// post api call to register new user in database
		mockMvc.perform(post(ApiRoutes.BASE + ApiRoutes.V1 + ApiRoutes.ANON_BASE + ApiRoutes.ANON_REGISTER)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new RegisterDTO(
								"UserToRegister",
								"emailRegiste2@gmail.com",
								"emailRegister@gmail.com",
								123456789,
								"Password1",
								"Password1")

						)))
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("email");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.EMAIL_NO_MATCH);
						}
				);
	}

	@Test
	void givenRegisterPostApiCall_whenInvalidEmail_thenThrowException() throws Exception {
		// Act

		// post api call to register new user in database
		mockMvc.perform(post(ApiRoutes.BASE + ApiRoutes.V1 + ApiRoutes.ANON_BASE + ApiRoutes.ANON_REGISTER)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new RegisterDTO(
								"UserToRegister",
								"emailRegister$·%·$$gmail.com",
								"emailRegister$·%·$$gmail.com",
								123456789,
								"Password1",
								"Password1")
						)))
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("email");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.EMAIL_INVALID);
						}
				);
	}

	@Test
	void givenRegisterPostApiCall_whenNonMatchingPassword_thenThrowException() throws Exception {
		// Act

		// post api call to register new user in database
		mockMvc.perform(post(ApiRoutes.BASE + ApiRoutes.V1 + ApiRoutes.ANON_BASE + ApiRoutes.ANON_REGISTER)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new RegisterDTO(
								"UserToRegister",
								"emailRegister@gmail.com",
								"emailRegister@gmail.com",
								123456789,
								"Password1",
								"Password12")
						)))
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("password");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.PASSWORD_NO_MATCH);
						}
				);
	}

	@Test
	void givenRegisterPostApiCall_whenInvalidPassword_thenThrowException() throws Exception {
		// Act

		// post api call to register new user in database
		mockMvc.perform(post(ApiRoutes.BASE + ApiRoutes.V1 + ApiRoutes.ANON_BASE + ApiRoutes.ANON_REGISTER)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new RegisterDTO(
								"UserToRegister",
								"emailRegister@gmail.com",
								"emailRegister@gmail.com",
								123456789,
								"Password",
								"Password")
						)))
				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("password");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.PASSWORD_INVALID);
						}
				);
	}

	void createUserTestSubject(RegisterDTO registerDTO) throws Exception {
		mockMvc.perform(post(ApiRoutes.BASE + ApiRoutes.V1 + ApiRoutes.ANON_BASE + ApiRoutes.ANON_REGISTER)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerDTO)))
				.andExpect(status().isCreated());
	}
}