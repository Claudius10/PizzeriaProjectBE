package PizzaApp.api.user;

import PizzaApp.api.configs.security.utils.SecurityCookieUtils;
import PizzaApp.api.configs.security.utils.SecurityTokenUtils;
import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.dto.error.ApiErrorDTO;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.repos.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserIdValidationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SecurityTokenUtils securityTokenUtils;

	@Autowired
	private UserRepository userRepository;

	public Long createUserTestSubject(RegisterDTO registerDTO) throws Exception {
		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerDTO))
						.with(csrf()))
				.andExpect(status().isOk());

		Optional<User> user = userRepository.findByEmail(registerDTO.email());
		return user.map(User::getId).orElse(null);
	}

	// test for ValidateUserIdentity aspect
	@Test
	public void givenNonMatchingUserIdCookieAndJwtUserIdClaim_whenAccessingProtectedResource_thenReturnUnauthorized() throws Exception {
		// create user test subject

		long testUserId = createUserTestSubject(new RegisterDTO(
				"UserIdValidation",
				"UserIdValidationTestNonMatchingCookieUserIdAndJwtuserId@gmail.com",
				"UserIdValidationTestNonMatchingCookieUserIdAndJwtuserId@gmail.com",
				"password",
				"password"));

		// create JWT token

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"UserIdValidationTestNonMatchingCookieUserIdAndJwtuserId@gmail.com",
				testUserId,
				"USER");

		String response = mockMvc.perform(get("/api/user/{userId}", testUserId)
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(3421321L), 1800, false, false))
						.with(csrf()))
				.andExpect(status().isUnauthorized()).andReturn().getResponse().getContentAsString();

		ApiErrorDTO apiErrorDTO = objectMapper.readValue(response, ApiErrorDTO.class);
		assertEquals("Access denied: fraudulent request", apiErrorDTO.errorMsg());
	}

	@Test
	public void givenMissingUserIdCookie_whenAccessingProtectedResource_thenReturnUnauthorized() throws Exception {
		// create user test subject

		long testUserId = createUserTestSubject(new RegisterDTO(
				"UserIdValidation",
				"UserIdValidationTestMissingUserIdCookie@gmail.com",
				"UserIdValidationTestMissingUserIdCookie@gmail.com",
				"password",
				"password"));

		// create JWT token

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"UserIdValidationTestMissingUserIdCookie@gmail.com",
				testUserId,
				"USER");

		String response = mockMvc.perform(get("/api/user/orders/{orderId}", testUserId)
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.with(csrf()))
				.andExpect(status().isUnauthorized()).andReturn().getResponse().getContentAsString();

		ApiErrorDTO apiErrorDTO = objectMapper.readValue(response, ApiErrorDTO.class);
		assertEquals("Access denied: unable to verify user identity", apiErrorDTO.errorMsg());
	}

	@Test
	public void givenMatchingUserIdCookieAndJWTUserIdClaim_whenAccessingProtectedResource_thenReturnOk() throws Exception {
		// create user test subject

		long testUserId = createUserTestSubject(new RegisterDTO(
				"UserIdValidation",
				"UserIdValidationTestMatchingUserIdCookieAndJwtUserIdClaim@gmail.com",
				"UserIdValidationTestMatchingUserIdCookieAndJwtUserIdClaim@gmail.com",
				"password",
				"password"));

		// create JWT token

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"UserIdValidationTestMatchingUserIdCookieAndJwtUserIdClaim@gmail.com",
				testUserId,
				"USER");

		mockMvc.perform(get("/api/user/{userId}", testUserId)
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(testUserId), 1800, false, false))
						.with(csrf()))
				.andExpect(status().isOk());
	}
}

