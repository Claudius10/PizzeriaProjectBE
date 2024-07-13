package PizzaApp.api.security;

import PizzaApp.api.configs.security.utils.SecurityCookieUtils;
import PizzaApp.api.configs.security.utils.SecurityTokenUtils;
import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.utils.globals.SecurityResponses;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@DirtiesContext
class UserIdValidationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SecurityTokenUtils securityTokenUtils;

	@Autowired
	private JdbcTemplate jdbcTemplate;

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

		long testUserId = createUserTestSubject(new RegisterDTO(
				"UserIdValidation",
				"UserIdValidationTestNonMatchingCookieUserIdAndJwtuserId@gmail.com",
				"UserIdValidationTestNonMatchingCookieUserIdAndJwtuserId@gmail.com",
				"Password1",
				"Password1"));

		// create JWT token

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"UserIdValidationTestNonMatchingCookieUserIdAndJwtuserId@gmail.com",
				testUserId,
				"USER");

		Long nonMatchingUserId = 9999L;

		// Act

		MockHttpServletResponse response = mockMvc.perform(get("/api/user/{userId}", testUserId)
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(nonMatchingUserId), 1800, false, false))
						.with(csrf()))
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		assertThat(response.getContentAsString()).isEqualTo(SecurityResponses.FRAUDULENT_TOKEN);
	}

	@Test
	void givenAccessToProtectedResource_whenMissingUserIdCookie_thenReturnUnauthorized() throws Exception {

		// Arrange

		// create user test subject

		long testUserId = createUserTestSubject(new RegisterDTO(
				"UserIdValidation",
				"UserIdValidationTestMissingUserIdCookie@gmail.com",
				"UserIdValidationTestMissingUserIdCookie@gmail.com",
				"Password1",
				"Password1"));

		// create JWT token

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"UserIdValidationTestMissingUserIdCookie@gmail.com",
				testUserId,
				"USER");

		// Act

		MockHttpServletResponse response = mockMvc.perform(get("/api/user/orders/{orderId}", testUserId)
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.with(csrf()))
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		assertThat(response.getContentAsString()).isEqualTo(SecurityResponses.USER_ID_MISSING);
	}

	@Test
	void givenAccessToProtectedResource_whenAllOk_thenReturnOk() throws Exception {

		// Arrange

		// create user test subject

		long testUserId = createUserTestSubject(new RegisterDTO(
				"UserIdValidation",
				"UserIdValidationTestMatchingUserIdCookieAndJwtUserIdClaim@gmail.com",
				"UserIdValidationTestMatchingUserIdCookieAndJwtUserIdClaim@gmail.com",
				"Password1",
				"Password1"));

		// create JWT token

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"UserIdValidationTestMatchingUserIdCookieAndJwtUserIdClaim@gmail.com",
				testUserId,
				"USER");

		// Act

		MockHttpServletResponse response = mockMvc.perform(get("/api/user/{userId}", testUserId)
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(testUserId), 1800, false, false))
						.with(csrf()))
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}

	Long createUserTestSubject(RegisterDTO registerDTO) throws Exception {
		return Long.valueOf(mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerDTO))
						.with(csrf()))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString());
	}
}