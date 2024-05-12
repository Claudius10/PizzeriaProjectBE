package PizzaApp.api.controller.locked;

import PizzaApp.api.configs.security.utils.SecurityCookieUtils;
import PizzaApp.api.configs.security.utils.SecurityTokenUtils;
import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.dto.error.ApiErrorDTO;
import PizzaApp.api.entity.user.dto.UserDTO;
import PizzaApp.api.repos.address.AddressRepository;
import PizzaApp.api.repos.role.RoleRepository;
import PizzaApp.api.repos.user.UserRepository;
import PizzaApp.api.utils.globals.SecurityResponses;
import PizzaApp.api.utils.globals.ValidationResponses;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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
				"accessToken@gmail.com",
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
				"accessToken@gmail.com",
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
		createUserTestSubject();
		Long userId = userRepository.findUserByEmail("Tester@gmail.com").getId();

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"user@gmail.com",
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
		createUserTestSubject();
		Long userId = userRepository.findUserByEmail("Tester@gmail.com").getId();

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"user@gmail.com",
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
				"user@gmail.com",
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
		createUserTestSubject();
		Long userId = userRepository.findUserByEmail("Tester@gmail.com").getId();

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"user@gmail.com",
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

		assertThat(response.getContentAsString()).isEqualTo(ValidationResponses.MAX_ADDRESS_SIZE);
	}

	@Test
	public void givenUserAddressListGetApiCall_thenReturnUserAddressList() throws Exception {
		// Arrange

		// post api call to register new user in database
		createUserTestSubject();
		Long userId = userRepository.findUserByEmail("Tester@gmail.com").getId();

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"user@gmail.com",
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
				"user@gmail.com",
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
		createUserTestSubject();
		Long userId = userRepository.findUserByEmail("Tester@gmail.com").getId();

		// create address object
		Address address = new Address.Builder()
				.withStreet("Street")
				.withStreetNr(1)
				.build();

		// create access token
		String accessToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"user@gmail.com",
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
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withIgnoreNullValues()
				.withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
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

	public void createUserTestSubject() throws Exception {
		mockMvc.perform(post("/api/anon/register").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new RegisterDTO(
						"Tester",
						"Tester@gmail.com",
						"Tester@gmail.com",
						"Password1",
						"Password1"))));
	}
}