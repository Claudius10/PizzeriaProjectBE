package org.pizzeria.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pizzeria.api.configs.web.security.auth.JWTTokenManager;
import org.pizzeria.api.configs.web.security.utils.SecurityCookieUtils;
import org.pizzeria.api.entity.role.Role;
import org.pizzeria.api.entity.user.User;
import org.pizzeria.api.repos.user.UserRepository;
import org.pizzeria.api.web.dto.api.Response;
import org.pizzeria.api.web.dto.auth.RegisterDTO;
import org.pizzeria.api.web.globals.ApiRoutes;
import org.pizzeria.api.web.globals.Constants;
import org.pizzeria.api.web.globals.SecurityResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.pizzeria.api.utils.TestUtils.getResponse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@Sql(scripts = {"file:src/test/resources/db/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@DirtiesContext
class UserIdValidationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private JWTTokenManager JWTTokenManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UserRepository userRepository;

	@BeforeAll
	@AfterAll
	void cleanUp() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, "users_roles", "users_addresses", "user");
	}

	// test for ValidateUserIdentity aspect
	@Test
	void givenAccessToProtectedResource_whenNonMatchingUserIdCookieAndJwtUserIdClaim_thenReturnUnauthorized() throws Exception {

		// Arrange

		// create user test subject

		long testUserId = createUser(new RegisterDTO(
				"UserIdValidation",
				"UserIdValidationTestNonMatchingCookieUserIdAndJwtuserId@gmail.com",
				"UserIdValidationTestNonMatchingCookieUserIdAndJwtuserId@gmail.com",
				"Password1",
				"Password1"));

		// create JWT token
		String accessToken = JWTTokenManager.getAccessToken(
				"UserIdValidationTestNonMatchingCookieUserIdAndJwtuserId@gmail.com",
				List.of(new Role("USER")),
				testUserId);

		Long nonMatchingUserId = 9999L;

		// Act

		MockHttpServletResponse response = mockMvc.perform(get(
						ApiRoutes.BASE + ApiRoutes.V1 + ApiRoutes.USER_BASE + ApiRoutes.USER_ID, nonMatchingUserId)
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 1800, true, false)))
				.andReturn()
				.getResponse();

		// Assert

		Response responseObj = getResponse(response, objectMapper);
		assertThat(responseObj.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		assertThat(responseObj.getErrorClass()).isEqualTo(SecurityResponses.USER_ID_NO_MATCH);
	}

	@Test
	void givenAccessToProtectedResource_whenAllOk_thenReturnOk() throws Exception {

		// Arrange

		// create user test subject

		long testUserId = createUser(new RegisterDTO(
				"UserIdValidation",
				"UserIdValidationTestMatchingUserIdCookieAndJwtUserIdClaim@gmail.com",
				"UserIdValidationTestMatchingUserIdCookieAndJwtUserIdClaim@gmail.com",
				"Password1",
				"Password1"));

		// create JWT token

		String accessToken = JWTTokenManager.getAccessToken(
				"UserIdValidationTestMatchingUserIdCookieAndJwtUserIdClaim@gmail.com",
				List.of(new Role("USER")),
				testUserId);

		// Act

		MockHttpServletResponse response = mockMvc.perform(get(
						ApiRoutes.BASE + ApiRoutes.V1 + ApiRoutes.USER_BASE + ApiRoutes.USER_ID, testUserId)
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 1800, true, false)))
				.andReturn()
				.getResponse();

		// Assert

		Response responseObj = getResponse(response, objectMapper);
		assertThat(responseObj.getStatusCode()).isEqualTo(HttpStatus.OK.value());
	}

	Long createUser(RegisterDTO registerDTO) throws Exception {
		mockMvc.perform(post(
				ApiRoutes.BASE
						+ ApiRoutes.V1
						+ ApiRoutes.ANON_BASE
						+ ApiRoutes.ANON_REGISTER)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerDTO)
				));

		Optional<User> user = userRepository.findUserByEmailWithRoles(registerDTO.email());
		assertThat(user.isPresent()).isTrue();
		return user.get().getId();
	}
}