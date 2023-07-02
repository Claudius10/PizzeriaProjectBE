package PizzaApp.api.security;

import PizzaApp.api.utility.auth.CookieUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
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
	private JwtEncoder jwtEncoder;

	private String validAccessToken, expiredAccessToken, validRefreshToken, expiredRefreshToken, validTokenWithNoIssuer;

	@BeforeAll
	public void setup() {

		validAccessToken = jwtEncoder.encode(
						JwtEncoderParameters.from(
								JwtClaimsSet.builder()
										.issuer("http://192.168.1.11:8090")
										.subject("test subject")
										.issuedAt(Instant.now())
										.expiresAt(Instant.now().plus(30, ChronoUnit.SECONDS))
										.claim("roles", "USER")
										.build()))
				.getTokenValue();

		expiredAccessToken = jwtEncoder.encode(
						JwtEncoderParameters.from(
								JwtClaimsSet.builder()
										.issuer("http://192.168.1.11:8090")
										.subject("test subject")
										.issuedAt(Instant.now().minus(30, ChronoUnit.MINUTES))
										.expiresAt(Instant.now().minus(10, ChronoUnit.MINUTES))
										.claim("roles", "USER")
										.build()))
				.getTokenValue();

		validRefreshToken = jwtEncoder.encode(
						JwtEncoderParameters.from(
								JwtClaimsSet.builder()
										.issuer("http://192.168.1.11:8090")
										.subject("test subject")
										.issuedAt(Instant.now())
										.expiresAt(Instant.now().plus(60, ChronoUnit.SECONDS))
										.claim("roles", "USER")
										.build()))
				.getTokenValue();

		expiredRefreshToken = jwtEncoder.encode(
						JwtEncoderParameters.from(
								JwtClaimsSet.builder()
										.issuer("http://192.168.1.11:8090")
										.subject("test subject")
										.issuedAt(Instant.now().minus(30, ChronoUnit.MINUTES))
										.expiresAt(Instant.now().minus(10, ChronoUnit.MINUTES))
										.claim("roles", "USER")
										.build()))
				.getTokenValue();


		validTokenWithNoIssuer = jwtEncoder.encode(
						JwtEncoderParameters.from(
								JwtClaimsSet.builder()
										.subject("test subject")
										.issuedAt(Instant.now())
										.expiresAt(Instant.now().plus(30, ChronoUnit.SECONDS))
										.claim("roles", "USER")
										.build()))
				.getTokenValue();

	}

	@Test
	public void givenCsrfProtection_whenRequestingResourceWithNoCsrfToken_thenUnauthorized() throws Exception {
		logger.info("JWT Token test: access secure unsafe http method with no csrf token");

		mockMvc.perform(post("/api/account/test")
						.cookie(CookieUtils.cookie("fight", validAccessToken, 30, true, false)))
				.andExpect(status().isUnauthorized());

		logger.info("JWT Token test: successfully NOT accessed secure unsafe http method with no csrf token");
	}

	@Test
	public void givenCsrfProtection_whenRequestingResourceWithCsrfToken_thenAccessResource() throws Exception {
		logger.info("JWT Token test: access secure unsafe http method with csrf token");

		mockMvc.perform(post("/api/account/test")
						.cookie(CookieUtils.cookie("fight", validAccessToken, 30, true, false))
						.with(csrf()))
				.andExpect(status().isOk());

		logger.info("JWT Token test: successfully accessed secure unsafe http method with csrf token");
	}

	@Test
	public void givenCredentials_whenRequestingLogout_thenEraseCredentials() throws Exception {
		logger.info("JWT Token test: request logout");

		MockHttpServletResponse response = mockMvc.perform(post("/api/auth/logout")
						.cookie(CookieUtils.cookie("fight", validAccessToken, 30, true, false))
						.cookie(CookieUtils.cookie("me", validRefreshToken, 60, true, false))
						.cookie(CookieUtils.cookie("email", "test", 30, false, false))
						.cookie(CookieUtils.cookie("id", "1", 30, false, false))
						.with(csrf()))
				.andExpect(status().isOk()).andReturn().getResponse();

		assertAll(() -> {
			assertEquals(0, response.getCookie("fight").getMaxAge());
			assertEquals("", response.getCookie("fight").getValue());
			assertEquals(0, response.getCookie("me").getMaxAge());
			assertEquals("", response.getCookie("me").getValue());
			assertEquals(0, response.getCookie("email").getMaxAge());
			assertEquals("", response.getCookie("email").getValue());
			assertEquals(0, response.getCookie("id").getMaxAge());
			assertEquals("", response.getCookie("id").getValue());
		});

		logger.info("JWT Token test: successfully erased credentials after logout");
	}

	@Test
	public void givenValidTokenAndRole_whenRequestingProtectedResource_thenAccessResource() throws Exception {
		logger.info("JWT Token test: access protected resource with valid token and correct role");

		mockMvc.perform(get("/api/account/test")
						.cookie(CookieUtils.cookie("fight", validAccessToken, 30, true, false)))
				.andExpect(status().isOk());

		logger.info("JWT Token test: successfully accessed resource");
	}

	@Test
	public void givenInvalidRole_whenRequestingProtectedResource_thenThrow() throws Exception {
		logger.info("JWT Token test: access protected resource with valid token and incorrect role");

		mockMvc.perform(get("/api/admin/test")
						.cookie(CookieUtils.cookie("fight", validAccessToken, 30, true, false)))
				.andExpect(status().isUnauthorized());

		logger.info("JWT Token test: successfully NOT accessed resource with incorrect role");
	}

	@Test
	public void givenExpiredToken_whenRequestingProtectedResource_thenThrow() throws Exception {
		logger.info("JWT Token test: access protected resource with expired token");

		mockMvc.perform(get("/api/account/test")
						.cookie(CookieUtils.cookie("fight", expiredAccessToken, 30, true, false)))
				.andExpect(status().isUnauthorized());

		logger.info("JWT Token test: successfully NOT accessed resource with expired token");
	}

	@Test
	public void givenNoToken_whenRequestingProtectedResource_thenThrow() throws Exception {
		logger.info("JWT Token test: access protected resource without token");

		mockMvc.perform(get("/api/admin"))
				.andExpect(status().isUnauthorized());

		logger.info("JWT Token test: successfully NOT accessed resource without token");
	}

	@Test
	public void givenTokenWithNoIssuer_whenRequestingProtectedResource_thenThrow() throws Exception {
		logger.info("JWT Token test: access protected resource with token that has no issuer");

		mockMvc.perform(get("/api/account/test")
						.cookie(CookieUtils.cookie("fight", validTokenWithNoIssuer, 30, true, false)))
				.andExpect(status().isUnauthorized());

		logger.info("JWT Token test: successfully NOT accessed resource with token that has no issuer");
	}
}
