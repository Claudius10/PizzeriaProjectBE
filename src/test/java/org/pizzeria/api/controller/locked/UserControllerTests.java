package org.pizzeria.api.controller.locked;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pizzeria.api.configs.security.auth.JWTTokenManager;
import org.pizzeria.api.configs.security.utils.SecurityCookieUtils;
import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.entity.dto.auth.RegisterDTO;
import org.pizzeria.api.entity.role.Role;
import org.pizzeria.api.entity.user.User;
import org.pizzeria.api.entity.user.dto.*;
import org.pizzeria.api.repos.address.AddressRepository;
import org.pizzeria.api.repos.user.UserRepository;
import org.pizzeria.api.utils.globals.ApiResponses;
import org.pizzeria.api.utils.globals.Constants;
import org.pizzeria.api.utils.globals.SecurityResponses;
import org.pizzeria.api.utils.globals.ValidationResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@Sql(scripts = {"file:src/test/resources/db/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@DirtiesContext
class UserControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private JWTTokenManager JWTTokenManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@AfterEach
	void cleanUp() {
		userRepository.deleteAll();
		addressRepository.deleteAll();
	}

	@Test
	void givenFindUserGetApiCall_whenUserNotFound_thenReturnAcceptedWithMessage() throws Exception {
		// Arrange

		// create access token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), 1L);

		// Act

		// get api call to find user
		MockHttpServletResponse response = mockMvc.perform(get("/api/user/{userId}", 1)
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());
		assertThat(response.getContentAsString()).isEqualTo(String.format(ApiResponses.USER_NOT_FOUND, 1));
	}

	@Test
	void givenFindUserGetApiCall_whenUserFound_thenReturnUser() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject("Tester@gmail.com");

		// create access token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userId);

		// Act

		MockHttpServletResponse response = mockMvc.perform(get("/api/user/{userId}", userId)
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		UserDTO userDTO = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), UserDTO.class);
		assertThat(userDTO.id()).isEqualTo(userId);
	}

	@Test
	void givenCreateAddressPostApiCall_thenCreateAddressAndReturnAccepted() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject("Tester@gmail.com");

		// create access token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userId);

		// create address object
		Address address = new Address.Builder()
				.withStreet("Street")
				.withGate("Gate")
				.withStaircase("StairCase")
				.withStreetNr(1)
				.build();

		// Act

		// post api call to add address to user
		MockHttpServletResponse response = mockMvc.perform(post("/api/user/{userId}/address", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(address))
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

		assertThat(userRepository.count()).isEqualTo(1);
		assertThat(addressRepository.count()).isEqualTo(1);

		Set<Address> userAddressList = userRepository.findUserAddressListById(userId);
		assertThat(userAddressList).hasSize(1);
	}

	@Test
	void givenCreateAddressPostApiCall_whenUserNotFound_thenReturnUnauthorizedWithMessage() throws Exception {
		// Arrange

		// create access token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), 1L);
		// create address object
		Address address = new Address.Builder()
				.withStreet("Street")
				.withStreetNr(1)
				.build();

		// Act

		// post api call to create address
		MockHttpServletResponse response = mockMvc.perform(post("/api/user/{userId}/address", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(address))
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		assertThat(response.getContentAsString()).isEqualTo(String.format(ApiResponses.USER_NOT_FOUND, 1));
	}

	@Test
	void givenCreateAddressPostApiCall_whenMaxAddressSize_thenReturnBadRequestStatusWithMessage() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject("Tester@gmail.com");

		// create access token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userId);

		// post api call to add address to user
		mockMvc.perform(post("/api/user/{userId}/address", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new Address.Builder()
						.withStreet("Street")
						.withStreetNr(1)
						.build()))
				.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)));

		// post api call to add address to user
		mockMvc.perform(post("/api/user/{userId}/address", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new Address.Builder()
						.withStreet("Street")
						.withStreetNr(2)
						.build()))
				.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)));

		// post api call to add address to user
		mockMvc.perform(post("/api/user/{userId}/address", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new Address.Builder()
						.withStreet("Street")
						.withStreetNr(3)
						.build()))
				.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)));

		// Act

		// post api call to add address to user
		MockHttpServletResponse response = mockMvc.perform(post("/api/user/{userId}/address", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new Address.Builder()
								.withStreet("Street")
								.withStreetNr(4)
								.build()))
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		assertThat(userRepository.count()).isEqualTo(1);
		assertThat(addressRepository.count()).isEqualTo(3);

		Set<Address> userAddressList = userRepository.findUserAddressListById(userId);
		assertThat(userAddressList).hasSize(3);

		assertThat(response.getContentAsString()).isEqualTo(ApiResponses.ADDRESS_MAX_SIZE);
	}

	@Test
	void givenUserAddressListGetApiCall_thenReturnUserAddressList() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject("Tester@gmail.com");

		// create access token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userId);

		// post api call to add address to user
		mockMvc.perform(post("/api/user/{userId}/address", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new Address.Builder()
						.withStreet("Street")
						.withStreetNr(1)
						.build()))
				.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)));

		// Act

		// get api call to find user address list
		MockHttpServletResponse response = mockMvc.perform(get("/api/user/{userId}/address", userId)
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

		assertThat(userRepository.count()).isEqualTo(1);
		assertThat(addressRepository.count()).isEqualTo(1);

		Set<Address> userAddressList = Set.of(objectMapper.readValue(response.getContentAsString(), Address[].class));
		assertThat(userAddressList).hasSize(1);
	}

	@Test
	void givenUserAddressListGetApiCall_whenNotFound_thenReturnAcceptedWithMessage() throws Exception {
		// Arrange

		// create access token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), 1L);

		// Act

		// get api call to find user address list
		MockHttpServletResponse response = mockMvc.perform(get("/api/user/{userId}/address", 1)
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());
		assertThat(response.getContentAsString()).isEqualTo(ApiResponses.ADDRESS_LIST_EMPTY);
	}

	@Test
	void givenDeleteUserAddressApiCall_thenDeleteUserAddress() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject("Tester@gmail.com");

		// create address object
		Address address = new Address.Builder()
				.withStreet("Street")
				.withStreetNr(1)
				.build();

		// create access token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userId);

		// post api call to add address to user
		mockMvc.perform(post("/api/user/{userId}/address", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(address))
				.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)));

		// confirm address was added
		Set<Address> userAddressList = userRepository.findUserAddressListById(userId);
		assertThat(userAddressList).hasSize(1);

		// find created address
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
		Optional<Address> dbAddress = addressRepository.findOne(Example.of(address, matcher));

		// Act

		// delete api call to delete user address
		MockHttpServletResponse response = mockMvc.perform(delete("/api/user/{userId}/address/{addressId}", userId,
						dbAddress.get().getId())
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		Set<Address> userAddressListAfterDelete = userRepository.findUserAddressListById(userId);
		assertThat(userAddressListAfterDelete).isEmpty();
	}

	@Test
	void givenDeleteUserAddressApiCall_whenUserAddressNotFound_thenReturnAcceptedWithMessage() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject("Tester@gmail.com");

		// create access token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userId);

		// Act

		// delete api call to delete user address
		MockHttpServletResponse response = mockMvc.perform(delete("/api/user/{userId}/address/{addressId}", userId, 1L)
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());
		assertThat(response.getContentAsString()).isEqualTo(ApiResponses.ADDRESS_NOT_FOUND);
	}

	@Test
	void givenDeleteUserAddressApiCall_whenUserNotFound_thenReturnUnauthorizedWithMessage() throws Exception {
		// Arrange

		// create access token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), 1L);

		// Act

		// delete api call to delete user address
		MockHttpServletResponse response = mockMvc.perform(delete("/api/user/{userId}/address/{addressId}", 1L, 1L)
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		assertThat(response.getContentAsString()).isEqualTo(String.format(ApiResponses.USER_NOT_FOUND, 1L));
	}

	@Test
	void givenNameUpdatePutApiCall_whenInvalidFormat_thenThrowError() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject("Tester@gmail.com");

		// create dto object
		NameChangeDTO nameChangeDTO = new NameChangeDTO("dsa$·", "Password1");

		// create access token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userId);

		// Act

		// put api call to update user name
		mockMvc.perform(put("/api/user/{userId}/name", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(nameChangeDTO))
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)))

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
	void givenNameUpdatePutApiCall_thenUpdateName() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject("Tester@gmail.com");

		// create dto object
		NameChangeDTO nameChangeDTO = new NameChangeDTO("NewUserName", "Password1");

		// create access token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userId);

		// Act

		// put api call to update user name
		MockHttpServletResponse response = mockMvc.perform(put("/api/user/{userId}/name", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(nameChangeDTO))
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		User user = userRepository.findUserByEmail("Tester@gmail.com");
		assertThat(user.getName()).isEqualTo(nameChangeDTO.name());
	}

	@Test
	void givenEmailUpdatePutApiCall_whenInvalidFormat_thenThrowError() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject("Tester@gmail.com");

		// create dto object
		EmailChangeDTO emailChangeDTO = new EmailChangeDTO("invalidEmailFormat", "Password1");

		// create access token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userId);

		// Act

		// put api call to update user email
		mockMvc.perform(put("/api/user/{userId}/email", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(emailChangeDTO))
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)))

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
	void givenEmailUpdatePutApiCall_thenUpdateEmail() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject("Tester@gmail.com");

		// create dto object
		EmailChangeDTO emailChangeDTO = new EmailChangeDTO("validEmailFormat@gmail.com", "Password1");

		// create access token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userId);

		// Act

		// put api call to update user email
		MockHttpServletResponse response = mockMvc.perform(put("/api/user/{userId}/email", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(emailChangeDTO))
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		User user = userRepository.findUserByEmail(emailChangeDTO.email());
		assertThat(user.getEmail()).isEqualTo(emailChangeDTO.email());
	}

	@Test
	void givenEmailUpdatePutApiCall_whenEmailAlreadyExists_thenReturnBadRequestWithMessage() throws Exception {
		// Arrange

		// post api call to register new user in database
		createUserTestSubject("Tester@gmail.com");
		Long userIdTwo = createUserTestSubject("Tester2@gmail.com");

		// create dto object
		EmailChangeDTO emailChangeDTO = new EmailChangeDTO("Tester@gmail.com", "Password1");

		// create access token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userIdTwo);

		// Act

		// put api call to update user email
		MockHttpServletResponse response = mockMvc.perform(put("/api/user/{userId}/email", userIdTwo)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(emailChangeDTO))
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getContentAsString()).isEqualTo(ApiResponses.EMAIL_ALREADY_EXISTS);
	}

	@Test
	void givenContactNumberUpdatePutApiCall_whenInvalidFormat_thenThrowError() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject("Tester@gmail.com");

		// create dto object
		ContactNumberChangeDTO contactNumberChangeDTO = new ContactNumberChangeDTO(123, "Password1");

		// create access token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userId);

		// Act

		// put api call to update user contact number
		mockMvc.perform(put("/api/user/{userId}/contact_number", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(contactNumberChangeDTO))
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)))

				// Assert

				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
							MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
							assert exception != null;
							List<FieldError> errors = exception.getBindingResult().getFieldErrors("contactNumber");
							assertThat(errors.getFirst().getDefaultMessage()).isEqualTo(ValidationResponses.NUMBER_INVALID);
						}
				);
	}

	@Test
	void givenContactNumberUpdatePutApiCall_thenUpdateContactNumber() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject("Tester@gmail.com");

		// create dto object
		ContactNumberChangeDTO contactNumberChangeDTO = new ContactNumberChangeDTO(123456789, "Password1");

		// create access token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userId);

		// Act

		// put api call to update user contact number
		MockHttpServletResponse response = mockMvc.perform(put("/api/user/{userId}/contact_number", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(contactNumberChangeDTO))
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		User user = userRepository.findUserByEmail("Tester@gmail.com");
		assertThat(user.getContactNumber()).isEqualTo(contactNumberChangeDTO.contactNumber());
	}

	@Test
	void givenContactNumberUpdatePutApiCall_whenNumberAlreadyExists_thenReturnBadRequestWithMessage() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject("Tester@gmail.com");
		Long userIdTwo = createUserTestSubject("Tester2@gmail.com");

		// create dto object
		ContactNumberChangeDTO contactNumberChangeDTO = new ContactNumberChangeDTO(123456789, "Password1");
		ContactNumberChangeDTO contactNumberChangeDTOTwo = new ContactNumberChangeDTO(123456789, "Password1");

		// create access token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userId);
		String accessTokenTwo = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userIdTwo);

		// Act

		// put api call to update user contact number
		mockMvc.perform(put("/api/user/{userId}/contact_number", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(contactNumberChangeDTO))
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 60, true, false)))
				.andReturn().getResponse();

		// put api call to update user contact number
		MockHttpServletResponse responseTwo = mockMvc.perform(put("/api/user/{userId}/contact_number", userIdTwo)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(contactNumberChangeDTOTwo))
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessTokenTwo, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(responseTwo.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(responseTwo.getContentAsString()).isEqualTo(ApiResponses.NUMBER_ALREADY_EXISTS);
	}

	@Test
	void givenUpdatePasswordPutApiCall_whenInvalidCurrentPassword_thenThrowBadCredentialsException() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject("Tester@gmail.com");

		// create JWT token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userId);

		// create dto object
		PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO(
				"Password",
				"Password2",
				"Password2");

		// Act

		// put api call to update password
		MockHttpServletResponse response = mockMvc.perform(put("/api/user/{userId}/password", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(passwordChangeDTO))
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 1800, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		assertThat(response.getContentAsString()).isEqualTo(SecurityResponses.BAD_CREDENTIALS);
	}

	@Test
	void givenUpdatePasswordPutApiCall_thenUpdateUserPassword() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject("Tester@gmail.com");

		// create JWT token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userId);

		// create dto object
		PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO(
				"Password1",
				"Password2",
				"Password2");

		// Act

		// put api call to update password
		MockHttpServletResponse response = mockMvc.perform(put("/api/user/{userId}/password", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(passwordChangeDTO))
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 1800, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		User user = userRepository.findUserByEmail("Tester@gmail.com");
		assertThat(passwordEncoder.matches(passwordChangeDTO.newPassword(), user.getPassword())).isTrue();
	}

	@Test
	void givenDeleteUserApiCall_whenInvalidCurrentPassword_thenThrowBadCredentialsException() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject("Tester@gmail.com");

		// create JWT token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userId);

		// create dto object
		PasswordDTO passwordDTO = new PasswordDTO("WrongPassword");

		// Act

		// delete api call to delete user
		MockHttpServletResponse response = mockMvc.perform(delete("/api/user/{userId}", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(passwordDTO))
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 1800, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		assertThat(response.getContentAsString()).isEqualTo(SecurityResponses.BAD_CREDENTIALS);
	}

	@Test
	void givenDeleteUserApiCall_thenDeleteUser() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject("Tester@gmail.com");

		// create JWT token
		String accessToken = JWTTokenManager.getAccessToken("Tester@gmail.com", List.of(new Role("USER")), userId);

		// create dto object
		PasswordDTO passwordDTO = new PasswordDTO("Password1");

		// Act

		// put api call to delete the user
		MockHttpServletResponse response = mockMvc.perform(delete("/api/user/{userId}", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(passwordDTO))
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 1800, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		Optional<User> user = userRepository.findUserByEmailWithRoles("Tester@gmail.com");
		assertThat(user).isEmpty();
	}

	Long createUserTestSubject(String email) throws Exception {
		return Long.valueOf(mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new RegisterDTO(
								"Tester",
								email,
								email,
								"Password1",
								"Password1"))))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString());
	}
}