package PizzaApp.api.security;

import PizzaApp.api.configs.security.utils.SecurityCookieUtils;
import PizzaApp.api.configs.security.utils.SecurityTokenUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtTokenTests {

	final Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SecurityTokenUtils securityTokenUtils;

	private String validAccessToken, validRefreshToken;

	@BeforeAll
	public void setup() {
		validAccessToken = securityTokenUtils.createToken(Instant.now().plus(30, ChronoUnit.SECONDS),
				"test subject",
				1L,
				"USER");

		validRefreshToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"test subject",
				1L,
				"USER");
	}

	@Test
	public void givenCredentials_whenRequestingLogout_thenEraseCredentials() throws Exception {
		logger.info("JWT Token test: request logout");

		MockHttpServletResponse response = mockMvc.perform(post("/api/auth/logout")
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("me", validRefreshToken, 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", "1", 30, false, false))
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

		logger.info("JWT Token test: successfully erased credentials after logout");
	}

	@Test
	public void givenCsrfProtection_whenRequestingResourceWithNoCsrfToken_thenUnauthorized() throws Exception {
		logger.info("JWT Token test: access csrf-protected resource with no csrf token");

		mockMvc.perform(post("/api/tests")
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 30, true, false)))
				.andExpect(status().isUnauthorized());

		logger.info("JWT Token test: successfully NOT accessed csrf-protected resource with no csrf token");
	}

	@Test
	public void givenCsrfProtection_whenRequestingResourceWithCsrfToken_thenAccessResource() throws Exception {
		logger.info("JWT Token test: access csrf-protected resource with csrf token");

		mockMvc.perform(post("/api/tests")
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.with(csrf()))
				.andExpect(status().isOk());

		logger.info("JWT Token test: successfully accessed csrf-protected resource with csrf token");
	}

	@Test
	public void givenValidTokenAndRole_whenRequestingProtectedResource_thenAccessResource() throws Exception {
		logger.info("JWT Token test: access secure resource with valid token and correct role");

		mockMvc.perform(get("/api/tests")
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 30, true, false)))
				.andExpect(status().isOk());

		logger.info("JWT Token test: successfully accessed resource");
	}

	@Test
	public void givenExpiredToken_whenRequestingProtectedResource_thenThrow() throws Exception {
		logger.info("JWT Token test: access secure resource with expired token");

		mockMvc.perform(get("/api/tests")).andExpect(status().isUnauthorized());

		logger.info("JWT Token test: successfully NOT accessed secure resource with expired token");
	}

	@Test
	public void givenExpiredAccessTokenAndValidRefreshToken_whenRequestingNewTokens_thenReturnNewTokens() throws Exception {
		logger.info("JWT Token test: request new tokens with expired access token and valid refresh token");

		MockHttpServletResponse response = mockMvc.perform(post("/api/token/refresh")
						.cookie(SecurityCookieUtils.makeCookie("me", validRefreshToken, 30, true, false))
						.with(csrf()))
				.andExpect(status().isOk()).andReturn().getResponse();

		assertAll(() -> {
			assertEquals(86400, response.getCookie("fight").getMaxAge());
			// expected value is the age of the cookie
			assertEquals(604800, response.getCookie("me").getMaxAge());
		});

		logger.info("JWT Token test: successfully received new tokens");
	}

	@Test
	public void givenExpiredAccessTokenAndExpiredRefreshToken_whenRequestingNewTokens_thenThrowError() throws Exception {
		logger.info("JWT Token test: request new tokens with expired access and refresh tokens");

		mockMvc.perform(post("/api/token/refresh").with(csrf())).andExpect(status().isBadRequest());

		logger.info("JWT Token test: successfully NOT received new tokens");
	}
}
