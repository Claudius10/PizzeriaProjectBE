package PizzaApp.api.security;

import PizzaApp.api.configs.security.utils.SecurityCookieUtils;
import PizzaApp.api.configs.security.utils.SecurityTokenUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class ApiSecurityTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SecurityTokenUtils securityTokenUtils;

	@Test
	public void givenLogoutApiCall_whenCredentialsArePresent_thenEraseCredentials() throws Exception {
		// Arrange

		// create access token
		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(30, ChronoUnit.SECONDS),
				"TokenTestRequestLogout@gmail.com",
				1L,
				"USER");

		// create refresh token
		String validRefreshToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"TokenTestRequestLogout@gmail.com",
				1L,
				"USER");

		// Act

		// post api call to log-out
		MockHttpServletResponse response = mockMvc.perform(post("/api/auth/logout")
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.cookie(SecurityCookieUtils.makeCookie("me", validRefreshToken, 60, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(1L), 30, false, false))
						.with(csrf()))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(200);

		assertThat(Objects.requireNonNull(response.getCookie("fight")).getMaxAge()).isEqualTo(0);
		assertThat(Objects.requireNonNull(response.getCookie("me")).getMaxAge()).isEqualTo(0);
		assertThat(Objects.requireNonNull(response.getCookie("id")).getMaxAge()).isEqualTo(0);

		assertThat(Objects.requireNonNull(response.getCookie("fight")).getValue()).isEqualTo("");
		assertThat(Objects.requireNonNull(response.getCookie("me")).getValue()).isEqualTo("");
		assertThat(Objects.requireNonNull(response.getCookie("id")).getValue()).isEqualTo("");
	}

	@Test
	public void givenCsrfProtectedApiCall_whenNoCsrfToken_thenReturnUnauthorized() throws Exception {
		// Arrange

		// create access token
		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(30, ChronoUnit.SECONDS),
				"TokenTestAccessProtectedResourceWithNoCsrfToken@gmail.com",
				0L,
				"USER");

		// Act

		// post api call to check csrf protection
		mockMvc.perform(post("/api/tests/security")
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 30, true, false)))

				// Assert

				.andExpect(status().isUnauthorized());
	}

	@Test
	public void givenCsrfProtectedApiCall_whenCsrfTokenOk_thenReturnOk() throws Exception {
		// Arrange

		// create access token
		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(30, ChronoUnit.SECONDS),
				"TokenTestAccessProtectedResourceWithCsrfToken@gmail.com",
				0L,
				"USER");

		// Act

		// post api call to check csrf protection
		mockMvc.perform(post("/api/tests/security")
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 30, true, false))
						.with(csrf()))

				// Assert

				.andExpect(status().isOk());
	}

	@Test
	public void givenApiCallToResource_whenValidAccessTokenAndRole_thenReturnOk() throws Exception {
		// Arrange

		// create access token
		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(30, ChronoUnit.SECONDS),
				"TokenTestAccessProtectedResourceWithValidTokenAndRole@gmail.com",
				0L,
				"USER");

		// Act

		// post api call to check csrf protection
		mockMvc.perform(get("/api/tests/security")
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 30, true, false)))

				// Assert

				.andExpect(status().isOk());
	}

	@Test
	public void givenApiCallToResource_whenValidAccessTokenAndInvalidRole_thenReturnUnauthorized() throws Exception {
		// Arrange

		// create access token
		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(30, ChronoUnit.SECONDS),
				"TokenTestAccessProtectedResourceWithValidTokenAndRole@gmail.com",
				0L,
				"USER");

		// Act

		// post api call to check csrf protection
		mockMvc.perform(get("/api/tests/security/admin")
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 30, true, false)))

				// Assert

				.andExpect(status().isUnauthorized());
	}

	@Test
	public void givenApiCallToResource_whenNoToken_thenReturnUnauthorized() throws Exception {
		// Act

		// get api call to check security
		mockMvc.perform(get("/api/tests/security"))

				// Assert

				.andExpect(status().isUnauthorized());
	}
}