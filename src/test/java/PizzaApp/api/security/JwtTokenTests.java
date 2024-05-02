package PizzaApp.api.security;

import PizzaApp.api.configs.security.utils.SecurityCookieUtils;
import PizzaApp.api.configs.security.utils.SecurityTokenUtils;
import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.dto.error.ApiErrorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtTokenTests {

	final Logger logger = Logger.getLogger(getClass().getName());

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

	public Long createUserTestSubject(RegisterDTO registerDTO) throws Exception {
		return Long.valueOf(mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerDTO))
						.with(csrf()))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
	}

	@Test
	public void givenCredentials_whenRequestingLogout_thenEraseCredentials() throws Exception {
		logger.info("JWT Token test: after logout delete user info from cookies");

		long testUserId = createUserTestSubject(new RegisterDTO(
				"JWTTokenTests",
				"TokenTestRequestLogout@gmail.com",
				"TokenTestRequestLogout@gmail.com",
				"password",
				"password"));

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(30, ChronoUnit.SECONDS),
				"TokenTestRequestLogout@gmail.com",
				testUserId,
				"USER");

		String validRefreshToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"TokenTestRequestLogout@gmail.com",
				testUserId,
				"USER");

		MockHttpServletResponse response = mockMvc.perform(post("/api/auth/logout")
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("me", validRefreshToken, 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(testUserId), 30, false, false))
						.with(csrf()))
				.andExpect(status().isOk()).andReturn().getResponse();

		assertAll(() -> {
			assertEquals(0, response.getCookie("fight").getMaxAge());
			assertEquals("", response.getCookie("fight").getValue());
			assertEquals(0, response.getCookie("me").getMaxAge());
			assertEquals("", response.getCookie("me").getValue());
			assertEquals(0, response.getCookie("id").getMaxAge());
			assertEquals("", response.getCookie("id").getValue());
		});

		logger.info("JWT Token test: successfully deleted user info after logout");
	}

	@Test
	public void givenCsrfProtection_whenRequestingResourceWithNoCsrfToken_thenUnauthorized() throws Exception {
		logger.info("JWT Token test: access csrf-protected resource with no csrf token");

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(30, ChronoUnit.SECONDS),
				"TokenTestAccessProtectedResourceWithNoCsrfToken@gmail.com",
				0L,
				"USER");

		mockMvc.perform(post("/api/tests")
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 30, true, false)))
				.andExpect(status().isUnauthorized());

		logger.info("JWT Token test: successfully NOT accessed csrf-protected resource with no csrf token");
	}

	@Test
	public void givenCsrfProtection_whenRequestingResourceWithCsrfToken_thenAccessResource() throws Exception {
		logger.info("JWT Token test: access csrf-protected resource with csrf token");

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(30, ChronoUnit.SECONDS),
				"TokenTestAccessProtectedResourceWithCsrfToken@gmail.com",
				0L,
				"USER");

		mockMvc.perform(post("/api/tests")
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.with(csrf()))
				.andExpect(status().isOk());

		logger.info("JWT Token test: successfully accessed csrf-protected resource with csrf token");
	}

	@Test
	public void givenValidTokenAndRole_whenRequestingProtectedResource_thenAccessResource() throws Exception {
		logger.info("JWT Token test: access secure resource with valid token and correct role");

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(30, ChronoUnit.SECONDS),
				"TokenTestAccessProtectedResourceWithValidTokenAndRole@gmail.com",
				0L,
				"USER");

		mockMvc.perform(get("/api/tests")
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 30, true, false)))
				.andExpect(status().isOk());

		logger.info("JWT Token test: successfully accessed resource");
	}

	@Test
	public void givenExpiredTokens_whenRequestingProtectedResource_thenReturnUnauthorized() throws Exception {
		logger.info("JWT Token test: access secure resource with expired tokens");

		mockMvc.perform(get("/api/tests")).andExpect(status().isUnauthorized());

		logger.info("JWT Token test: successfully NOT accessed secure resource with expired tokens");
	}

	@Test
	public void givenExpiredAccessTokenAndValidRefreshToken_whenRequestingNewTokens_thenReturnNewTokens() throws Exception {
		logger.info("JWT Token test: request new tokens with expired access token and valid refresh token");

		long testUserId = createUserTestSubject(new RegisterDTO(
				"JWTTokenTests",
				"TokenTestRequestNewTokensWithValidRefreshToken@gmail.com",
				"TokenTestRequestNewTokensWithValidRefreshToken@gmail.com",
				"password",
				"password"));

		String validRefreshToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"TokenTestRequestNewTokensWithValidRefreshToken@gmail.com",
				testUserId,
				"USER");

		MockHttpServletResponse response = mockMvc.perform(post("/api/token/refresh")
						.cookie(SecurityCookieUtils.makeCookie("me", validRefreshToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(testUserId), 30, false, false))
						.with(csrf()))
				.andExpect(status().isOk()).andReturn().getResponse();

		assertAll(() -> {
			assertEquals(86400, response.getCookie("fight").getMaxAge());
			assertEquals(604800, response.getCookie("me").getMaxAge());
			assertEquals(604800, response.getCookie("id").getMaxAge());
		});

		logger.info("JWT Token test: successfully received new tokens");
	}

	@Test
	public void givenExpiredAccessTokenAndValidRefreshTokenAndNonMatchingUserId_whenRequestingNewTokens_thenDontReturnNewTokens() throws Exception {
		logger.info("JWT Token test: request new tokens with expired access token and valid refresh token, but non matching " +
				"user id");

		long testUserId = createUserTestSubject(new RegisterDTO(
				"JWTTokenTests",
				"RequestNewTokensWithValidRefreshTokenAndNonMatchingUserId@gmail.com",
				"RequestNewTokensWithValidRefreshTokenAndNonMatchingUserId@gmail.com",
				"password",
				"password"));

		String validRefreshToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"RequestNewTokensWithValidRefreshTokenAndNonMatchingUserId@gmail.com",
				testUserId,
				"USER");

		String response = mockMvc.perform(post("/api/token/refresh")
						.cookie(SecurityCookieUtils.makeCookie("me", validRefreshToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(0), 30, false, false))
						.with(csrf()))
				.andExpect(status().isUnauthorized()).andReturn().getResponse().getContentAsString();

		ApiErrorDTO error = objectMapper.readValue(response, ApiErrorDTO.class);

		assertEquals("Access denied: fraudulent request", error.errorMsg());

		logger.info("JWT Token test: successfully NOT received new tokens");
	}

	@Test
	public void givenExpiredAccessTokenAndValidRefreshTokenAndNoUserIdCookie_whenRequestingNewTokens_thenDontReturnNewTokens() throws Exception {
		logger.info("JWT Token test: request new tokens with expired access token and valid refresh token, but no id cookie " +
				"present");

		long testUserId = createUserTestSubject(new RegisterDTO(
				"JWTTokenTests",
				"RequestNewTokensWithValidRefreshTokenAndNoIdCookiePresent@gmail.com",
				"RequestNewTokensWithValidRefreshTokenAndNoIdCookiePresent@gmail.com",
				"password",
				"password"));

		String validRefreshToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"TokenTestRequestNewTokensWithValidRefreshTokenAndNoIdCookiePresent@gmail.com",
				testUserId,
				"USER");

		String response = mockMvc.perform(post("/api/token/refresh")
						.cookie(SecurityCookieUtils.makeCookie("me", validRefreshToken, 30, true, false))
						.with(csrf()))
				.andExpect(status().isUnauthorized()).andReturn().getResponse().getContentAsString();

		ApiErrorDTO error = objectMapper.readValue(response, ApiErrorDTO.class);

		assertEquals("Access denied: unable to verify user identity", error.errorMsg());

		logger.info("JWT Token test: successfully NOT received new tokens");
	}

	@Test
	public void givenExpiredAccessTokenAndExpiredRefreshToken_whenRequestingNewTokens_thenDontReturnNewTokens() throws Exception {
		logger.info("JWT Token test: request new tokens with expired access and refresh tokens");

		mockMvc.perform(post("/api/token/refresh").with(csrf())).andExpect(status().isUnauthorized());

		logger.info("JWT Token test: successfully NOT received new tokens");
	}
}
