package org.pizzeria.api.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pizzeria.api.configs.web.security.auth.JWTTokenManager;
import org.pizzeria.api.configs.web.security.utils.SecurityCookieUtils;
import org.pizzeria.api.entity.role.Role;
import org.pizzeria.api.web.globals.ApiRoutes;
import org.pizzeria.api.web.globals.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class ApiSecurityTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JWTTokenManager JWTTokenManager;

	@Test
	void givenLogoutApiCall_whenCredentialsArePresent_thenEraseCredentials() throws Exception {
		// Arrange

		// create access token
		String validAccessToken = JWTTokenManager.getAccessToken(
				"TokenTestRequestLogout@gmail.com",
				List.of(new Role("USER")),
				1L);

		// Act

		// post api call to log-out
		MockHttpServletResponse response = mockMvc.perform(post(ApiRoutes.BASE + ApiRoutes.V1 + ApiRoutes.AUTH_BASE + ApiRoutes.AUTH_LOGOUT)
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, validAccessToken, 30, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(Objects.requireNonNull(response.getCookie(Constants.TOKEN_COOKIE_NAME)).getMaxAge()).isZero();
		assertThat(Objects.requireNonNull(response.getCookie(Constants.TOKEN_COOKIE_NAME)).getValue()).isEmpty();
	}

	@Test
	void givenApiCallToResource_whenValidAccessTokenAndRole_thenReturnOk() throws Exception {
		// Arrange

		// create access token
		String validAccessToken = JWTTokenManager.getAccessToken(
				"TokenTestAccessProtectedResourceWithValidTokenAndRole@gmail.com",
				List.of(new Role("USER")),
				0L);

		// Act

		// post api call to check csrf protection
		mockMvc.perform(get("/api/tests")
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, validAccessToken, 30, true, false)))

				// Assert

				.andExpect(status().isOk());
	}

	@Test
	void givenApiCallToResource_whenValidAccessTokenAndInvalidRole_thenReturnUnauthorized() throws Exception {
		// Arrange

		// create access token
		String validAccessToken = JWTTokenManager.getAccessToken(
				"TokenTestAccessProtectedResourceWithValidTokenAndRole@gmail.com",
				List.of(new Role("USER")),
				0L);

		// Act

		// post api call to check csrf protection
		mockMvc.perform(get("/api/tests/admin")
						.cookie(SecurityCookieUtils.prepareCookie(Constants.TOKEN_COOKIE_NAME, validAccessToken, 30, true, false)))

				// Assert

				.andExpect(status().isUnauthorized());
	}

	@Test
	void givenApiCallToResource_whenNoToken_thenReturnUnauthorized() throws Exception {
		// Act

		// get api call to check security
		mockMvc.perform(get("/api/tests"))

				// Assert

				.andExpect(status().isUnauthorized());
	}
}