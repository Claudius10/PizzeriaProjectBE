package PizzaApp.api.controller.locked;

import PizzaApp.api.configs.security.utils.SecurityCookieUtils;
import PizzaApp.api.configs.security.utils.SecurityTokenUtils;
import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.dto.error.ApiErrorDTO;
import PizzaApp.api.entity.role.Role;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.entity.user.dto.*;
import PizzaApp.api.repos.address.AddressRepository;
import PizzaApp.api.repos.role.RoleRepository;
import PizzaApp.api.repos.user.UserRepository;
import PizzaApp.api.utils.globals.SecurityResponses;
import PizzaApp.api.utils.globals.ValidationResponses;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@DirtiesContext
public class UserControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SecurityTokenUtils securityTokenUtils;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	public void cleanUp() {
		userRepository.deleteAll();
		addressRepository.deleteAll();
		roleRepository.deleteAll();
	}

	@Test
	public void givenFindUserGetApiCall_whenUserNotFound_thenReturnNotFound() throws Exception {
		// Arrange

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"Tester@gmail.com",
				1L,
				"USER");

		// Act

		// get api call to find user
		MockHttpServletResponse response = mockMvc.perform(get("/api/user/{userId}", 1).with(csrf())
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(1), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void givenFindUserGetApiCall_whenNoUserIdCookie_thenReturnUnauthorized() throws Exception {
		// Arrange

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"Tester@gmail.com",
				1L,
				"USER");

		// Act

		// get api call to find user
		MockHttpServletResponse response = mockMvc.perform(get("/api/user/{userId}", 1).with(csrf())
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		ApiErrorDTO error = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ApiErrorDTO.class);
		assertThat(error.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		assertThat(error.errorMsg()).isEqualTo(SecurityResponses.USER_ID_MISSING);
	}

	@Test
	public void givenFindUserGetApiCall_whenUserFound_thenReturnUser() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"Tester@gmail.com",
				userId,
				"USER");

		// Act

		MockHttpServletResponse response = mockMvc.perform(get("/api/user/{userId}", userId).with(csrf())
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		UserDTO userDTO = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), UserDTO.class);
		assertThat(userDTO.id()).isEqualTo(userId);
	}

	@Test
	public void givenCreateAddressPostApiCall_thenReturnOkStatus() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"Tester@gmail.com",
				userId,
				"USER");

		// create address object
		Address address = new Address.Builder()
				.withStreet("Street")
				.withStreetNr(1)
				.build();

		// Act

		// post api call to add address to user
		MockHttpServletResponse response = mockMvc.perform(post("/api/user/{userId}/address", userId).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(address))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

		assertThat(userRepository.count()).isEqualTo(1);
		assertThat(addressRepository.count()).isEqualTo(1);

		Set<Address> userAddressList = userRepository.findUserAddressListById(userId);
		assertThat(userAddressList.size()).isEqualTo(1);
	}

	@Test
	public void givenCreateAddressPostApiCall_whenUserNotFound_thenReturnNotFoundStatus() throws Exception {
		// Arrange

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"Tester@gmail.com",
				1L,
				"USER");

		// create address object
		Address address = new Address.Builder()
				.withStreet("Street")
				.withStreetNr(1)
				.build();

		// Act

		// post api call to create address
		MockHttpServletResponse response = mockMvc.perform(post("/api/user/{userId}/address", 1).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(address))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(1), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
		assertThat(response.getContentAsString()).isEqualTo(String.format(SecurityResponses.USER_NOT_FOUND, 1));
	}

	@Test
	public void givenCreateAddressPostApiCall_whenMaxAddressSize_thenReturnBadRequestStatus() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"Tester@gmail.com",
				userId,
				"USER");

		// post api call to add address to user
		mockMvc.perform(post("/api/user/{userId}/address", userId).with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new Address.Builder()
						.withStreet("Street")
						.withStreetNr(1)
						.build()))
				.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
				.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)));

		// post api call to add address to user
		mockMvc.perform(post("/api/user/{userId}/address", userId).with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new Address.Builder()
						.withStreet("Street")
						.withStreetNr(2)
						.build()))
				.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
				.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)));

		// post api call to add address to user
		mockMvc.perform(post("/api/user/{userId}/address", userId).with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new Address.Builder()
						.withStreet("Street")
						.withStreetNr(3)
						.build()))
				.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
				.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)));

		// Act

		// post api call to add address to user
		MockHttpServletResponse response = mockMvc.perform(post("/api/user/{userId}/address", userId).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new Address.Builder()
								.withStreet("Street")
								.withStreetNr(4)
								.build()))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		assertThat(userRepository.count()).isEqualTo(1);
		assertThat(addressRepository.count()).isEqualTo(3);

		Set<Address> userAddressList = userRepository.findUserAddressListById(userId);
		assertThat(userAddressList.size()).isEqualTo(3);

		assertThat(response.getContentAsString()).isEqualTo(ValidationResponses.ADDRESS_MAX_SIZE);
	}

	@Test
	public void givenUserAddressListGetApiCall_thenReturnUserAddressList() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"Tester@gmail.com",
				userId,
				"USER");

		// post api call to add address to user
		mockMvc.perform(post("/api/user/{userId}/address", userId).with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new Address.Builder()
						.withStreet("Street")
						.withStreetNr(1)
						.build()))
				.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
				.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)));

		// Act

		// get api call to find user address list
		MockHttpServletResponse response = mockMvc.perform(get("/api/user/{userId}/address", userId)
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

		assertThat(userRepository.count()).isEqualTo(1);
		assertThat(addressRepository.count()).isEqualTo(1);

		Set<Address> userAddressList = Set.of(objectMapper.readValue(response.getContentAsString(), Address[].class));
		assertThat(userAddressList.size()).isEqualTo(1);
	}

	@Test
	public void givenUserAddressListGetApiCall_whenNotFound_thenReturnNotFoundStatus() throws Exception {
		// Arrange

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"Tester@gmail.com",
				1L,
				"USER");

		// Act

		// get api call to find user address list
		MockHttpServletResponse response = mockMvc.perform(get("/api/user/{userId}/address", 1)
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(1), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void givenDeleteUserAddressApiCall_thenDeleteUserAddress() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create address object
		Address address = new Address.Builder()
				.withStreet("Street")
				.withStreetNr(1)
				.build();

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"Tester@gmail.com",
				userId,
				"USER");

		// post api call to add address to user
		mockMvc.perform(post("/api/user/{userId}/address", userId).with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(address))
				.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
				.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)));

		// confirm address was added
		Set<Address> userAddressList = userRepository.findUserAddressListById(userId);
		assertThat(userAddressList.size()).isEqualTo(1);

		// find created address
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
		Optional<Address> dbAddress = addressRepository.findOne(Example.of(address, matcher));

		// Act

		// delete api call to delete user address
		MockHttpServletResponse response = mockMvc.perform(delete("/api/user/{userId}/address/{addressId}", userId,
						dbAddress.get().getId()).with(csrf())
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		Set<Address> userAddressListAfterDelete = userRepository.findUserAddressListById(userId);
		assertThat(userAddressListAfterDelete.size()).isEqualTo(0);
	}

	@Test
	public void givenDeleteUserAddressApiCall_whenUserAddressNotFound_thenReturnNotFound() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create address object
		Address address = new Address.Builder()
				.withStreet("Street")
				.withStreetNr(1)
				.build();

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"Tester@gmail.com",
				userId,
				"USER");

		// Act

		// delete api call to delete user address
		MockHttpServletResponse response = mockMvc.perform(delete("/api/user/{userId}/address/{addressId}", userId, 1L).with(csrf())
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
		assertThat(response.getContentAsString()).isEqualTo(ValidationResponses.ADDRESS_NOT_FOUND);
	}

	@Test
	public void givenDeleteUserAddressApiCall_whenUserNotFound_thenReturnNotFound() throws Exception {
		// Arrange

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"Tester@gmail.com",
				1L,
				"USER");

		// Act

		// delete api call to delete user address
		MockHttpServletResponse response = mockMvc.perform(delete("/api/user/{userId}/address/{addressId}", 1L, 1L).with(csrf())
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(1L), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
		assertThat(response.getContentAsString()).isEqualTo(String.format(SecurityResponses.USER_NOT_FOUND, 1L));
	}

	@Test
	public void givenNameUpdatePutApiCall_whenInvalidFormat_thenThrowError() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create dto object
		NameChangeDTO nameChangeDTO = new NameChangeDTO("dsa$·", "Password1");

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"Tester@gmail.com",
				userId,
				"USER");

		// Act

		// put api call to update user name
		mockMvc.perform(put("/api/user/{userId}/name", userId).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(nameChangeDTO))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))

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
	public void givenNameUpdatePutApiCall_thenUpdateName() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create dto object
		NameChangeDTO nameChangeDTO = new NameChangeDTO("NewUserName", "Password1");

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"Tester@gmail.com",
				userId,
				"USER");

		// Act

		// put api call to update user name
		MockHttpServletResponse response = mockMvc.perform(put("/api/user/{userId}/name", userId).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(nameChangeDTO))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		User user = userRepository.findUserByEmail("Tester@gmail.com");
		assertThat(user.getName()).isEqualTo(nameChangeDTO.name());
	}

	@Test
	public void givenEmailUpdatePutApiCall_whenInvalidFormat_thenThrowError() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create dto object
		EmailChangeDTO emailChangeDTO = new EmailChangeDTO("invalidEmailFormat", "Password1");

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"Tester@gmail.com",
				userId,
				"USER");

		// Act

		// put api call to update user email
		mockMvc.perform(put("/api/user/{userId}/email", userId).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(emailChangeDTO))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))

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
	public void givenEmailUpdatePutApiCall_thenUpdateEmail() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create dto object
		EmailChangeDTO emailChangeDTO = new EmailChangeDTO("validEmailFormat@gmail.com", "Password1");

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"Tester@gmail.com",
				userId,
				"USER");

		// Act

		// put api call to update user email
		MockHttpServletResponse response = mockMvc.perform(put("/api/user/{userId}/email", userId).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(emailChangeDTO))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		User user = userRepository.findUserByEmail(emailChangeDTO.email());
		assertThat(user.getEmail()).isEqualTo(emailChangeDTO.email());
	}

	@Test
	public void givenContactNumberUpdatePutApiCall_whenInvalidFormat_thenThrowError() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create dto object
		ContactNumberChangeDTO contactNumberChangeDTO = new ContactNumberChangeDTO(123, "Password1");

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"Tester@gmail.com",
				userId,
				"USER");

		// Act

		// put api call to update user contact number
		mockMvc.perform(put("/api/user/{userId}/contact_number", userId).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(contactNumberChangeDTO))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))

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
	public void givenContactNumberUpdatePutApiCall_thenUpdateContactNumber() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create dto object
		ContactNumberChangeDTO contactNumberChangeDTO = new ContactNumberChangeDTO(123456789, "Password1");

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"Tester@gmail.com",
				userId,
				"USER");

		// Act

		// put api call to update user contact number
		MockHttpServletResponse response = mockMvc.perform(put("/api/user/{userId}/contact_number", userId).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(contactNumberChangeDTO))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("fight", accessToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		User user = userRepository.findUserByEmail("Tester@gmail.com");
		assertThat(user.getContactNumber()).isEqualTo(contactNumberChangeDTO.contactNumber());
	}

	@Test
	public void givenUpdatePasswordPutApiCall_whenInvalidCurrentPassword_thenThrowBadCredentialsException() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create JWT token
		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

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
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 1800, false, false))
						.with(csrf()))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		assertThat(response.getContentAsString()).isEqualTo(SecurityResponses.BAD_CREDENTIALS);
	}

	@Test
	public void givenUpdatePasswordPutApiCall_thenUpdateUserPassword() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create JWT token
		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

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
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 1800, false, false))
						.with(csrf()))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		User user = userRepository.findUserByEmail("Tester@gmail.com");
		assertThat(passwordEncoder.matches(passwordChangeDTO.newPassword(), user.getPassword())).isTrue();
	}

	@Test
	public void givenDeleteUserApiCall_whenInvalidCurrentPassword_thenThrowBadCredentialsException() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create JWT token
		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		// create dto object
		PasswordDTO passwordDTO = new PasswordDTO("WrongPassword");

		// Act

		// delete api call to delete user
		MockHttpServletResponse response = mockMvc.perform(delete("/api/user/{userId}", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(passwordDTO))
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 1800, false, false))
						.with(csrf()))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		assertThat(response.getContentAsString()).isEqualTo(SecurityResponses.BAD_CREDENTIALS);
	}

	@Test
	public void givenDeleteUserApiCall_thenDeleteUser() throws Exception {
		// Arrange

		// post api call to register new user in database
		Long userId = createUserTestSubject();

		// create JWT token
		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"Tester@gmail.com",
				userId,
				"USER");

		// create dto object
		PasswordDTO passwordDTO = new PasswordDTO("Password1");

		// Act

		// put api call to update password
		MockHttpServletResponse response = mockMvc.perform(delete("/api/user/{userId}", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(passwordDTO))
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(userId), 1800, false, false))
						.with(csrf()))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		Optional<User> user = userRepository.findUserByEmailWithRoles("Tester@gmail.com");
		assertThat(user.isEmpty()).isTrue();
	}

	public Long createUserTestSubject() throws Exception {
		if (roleRepository.findByName("USER") == null) {
			roleRepository.save(new Role("USER"));
		}

		return Long.valueOf(mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new RegisterDTO(
								"Tester",
								"Tester@gmail.com",
								"Tester@gmail.com",
								"Password1",
								"Password1")))
						.with(csrf()))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
	}
}