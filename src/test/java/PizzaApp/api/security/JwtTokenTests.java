package PizzaApp.api.security;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.junit.jupiter.api.Assertions.*;
import static PizzaApp.api.security.JwtTokenTests.BearerTokenRequestPostProcessor.bearerToken;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtTokenTests {

	final Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtEncoder jwtEncoder;

	private String validToken, expiredToken, tokenWithNoIssuer;

	@BeforeAll
	public void setup() {

		validToken = jwtEncoder.encode(
						JwtEncoderParameters.from(
								JwtClaimsSet.builder()
										.issuer("http://192.168.1.11:8090")
										.subject("test subject")
										.issuedAt(Instant.now())
										.expiresAt(Instant.now().plus(30, ChronoUnit.SECONDS))
										.claim("roles", "USER")
										.build()))
				.getTokenValue();

		expiredToken = jwtEncoder.encode(
						JwtEncoderParameters.from(
								JwtClaimsSet.builder()
										.issuer("http://192.168.1.11:8090")
										.subject("test subject")
										.issuedAt(Instant.now().minus(30, ChronoUnit.MINUTES))
										.expiresAt(Instant.now().minus(10, ChronoUnit.MINUTES))
										.claim("roles", "USER")
										.build()))
				.getTokenValue();

		tokenWithNoIssuer = jwtEncoder.encode(
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
	public void givenValidTokenAndRole_whenRequestingProtectedResource_thenReturnResource() throws Exception {
		logger.info("JWT Token test #1: access protected resource with valid token and correct role");

		assertEquals("User access", mockMvc.perform(get("/api/user")
						.with(bearerToken(validToken)))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString());

		logger.info("JWT Token test #1: successfully accessed resource");
	}

	@Test
	public void givenInvalidRole_whenRequestingProtectedResource_thenThrow() throws Exception {
		logger.info("JWT Token test #2: access protected resource with incorrect role");

		mockMvc.perform(get("/api/admin")
						.with(bearerToken(validToken)))
				.andExpect(status().isUnauthorized());

		logger.info("JWT Token test #2: successfully NOT accessed resource with incorrect role");
	}

	@Test
	public void givenExpiredToken_whenRequestingProtectedResource_thenThrow() throws Exception {
		logger.info("JWT Token test #3: access protected resource with expired token");

		mockMvc.perform(get("/api/admin")
						.with(bearerToken(expiredToken)))
				.andExpect(status().isUnauthorized());

		logger.info("JWT Token test #3: successfully NOT accessed resource with expired token");
	}

	@Test
	public void givenNoToken_whenRequestingProtectedResource_thenThrow() throws Exception {
		logger.info("JWT Token test #4: access protected resource without token");

		mockMvc.perform(get("/api/admin"))
				.andExpect(status().isUnauthorized());

		logger.info("JWT Token test #4: successfully NOT accessed resource without token");
	}

	@Test
	public void givenTokenWithNoIssuer_whenRequestingProtectedResource_thenThrow() throws Exception {
		logger.info("JWT Token test #5: access protected resource with token that has no issuer");

		mockMvc.perform(get("/api/admin")
						.with(bearerToken(tokenWithNoIssuer)))
				.andExpect(status().isUnauthorized());

		logger.info("JWT Token test #5: successfully NOT accessed resource with token that has no issuer");
	}

	// adds Authorization header to request with Bearer Token
	public static class BearerTokenRequestPostProcessor implements RequestPostProcessor {
		private final String token;

		public BearerTokenRequestPostProcessor(String token) {
			this.token = token;
		}

		@Override
		public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
			request.addHeader("Authorization", "Bearer " + this.token);
			return request;
		}

		public static BearerTokenRequestPostProcessor bearerToken(String token) {
			return new BearerTokenRequestPostProcessor(token);
		}
	}
}
