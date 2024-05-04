package PizzaApp.api.controller;

import PizzaApp.api.configs.security.utils.SecurityCookieUtils;
import PizzaApp.api.configs.security.utils.SecurityTokenUtils;
import PizzaApp.api.entity.dto.error.ApiErrorDTO;
import PizzaApp.api.utils.globals.SecurityResponses;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class TokenControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SecurityTokenUtils securityTokenUtils;

	@Test
	public void givenPostApiCall_whenRefreshTokenIsMissing_thenReturnUnauthorized() throws Exception {
		// Act & Assert

		mockMvc.perform(post("/api/token/refresh").with(csrf()))
				.andExpect(result -> {
							assertThat(result.getResponse().getStatus()).isEqualTo(401);
							String response = result.getResponse().getContentAsString();
							ApiErrorDTO apiError = objectMapper.readValue(response, ApiErrorDTO.class);
							assertThat(apiError.errorMsg()).isEqualTo(SecurityResponses.MISSING_TOKEN);
						}
				);
	}

	@Test
	public void givenPostApiCall_whenRefreshTokenIsOk_thenReturnRefreshedTokens() throws Exception {
		// Arrange

		String validRefreshToken = securityTokenUtils.createToken(Instant.now().plus(60, ChronoUnit.SECONDS),
				"TokenTestRequestLogout@gmail.com",
				1L,
				"USER");

		// Act

		MockHttpServletResponse response = mockMvc.perform(post("/api/token/refresh").with(csrf())
						.cookie(SecurityCookieUtils.makeCookie("me", validRefreshToken, 60, true, false)))
				.andReturn().getResponse();

		// Assert

		assertThat(response.getStatus()).isEqualTo(200);

		assertThat(Objects.requireNonNull(response.getCookie("fight")).getMaxAge()).isEqualTo(86400);
		assertThat(Objects.requireNonNull(response.getCookie("me")).getMaxAge()).isEqualTo(604800);
		assertThat(Objects.requireNonNull(response.getCookie("id")).getMaxAge()).isEqualTo(604800);
	}
}