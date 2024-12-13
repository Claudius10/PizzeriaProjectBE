package org.pizzeria.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pizzeria.api.configs.web.security.auth.JWTTokenManager;
import org.pizzeria.api.configs.web.security.utils.SecurityCookieUtils;
import org.pizzeria.api.web.dto.auth.RegisterDTO;
import org.pizzeria.api.entity.role.Role;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
		String accessToken = JWTTokenManager.getAccessToken(
				"UserIdValidationTestNonMatchingCookieUserIdAndJwtuserId@gmail.com",
				List.of(new Role("USER")),
				testUserId);

		Long nonMatchingUserId = 9999L;

		// Act

		MockHttpServletResponse response = mockMvc.perform(get("/api/user/{userId}", nonMatchingUserId)
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 1800, true, false)))
				.andReturn()
				.getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		assertThat(response.getContentAsString()).isEqualTo(SecurityResponses.USER_ID_NO_MATCH);
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

		String accessToken = JWTTokenManager.getAccessToken(
				"UserIdValidationTestMatchingUserIdCookieAndJwtUserIdClaim@gmail.com",
				List.of(new Role("USER")),
				testUserId);

		// Act

		MockHttpServletResponse response = mockMvc.perform(get("/api/user/{userId}", testUserId)
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, accessToken, 1800, true, false)))
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