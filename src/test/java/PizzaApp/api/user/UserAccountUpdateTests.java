package PizzaApp.api.user;

import PizzaApp.api.configs.security.utils.SecurityCookieUtils;
import PizzaApp.api.configs.security.utils.SecurityTokenUtils;
import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.entity.user.dto.PasswordChangeDTO;
import PizzaApp.api.services.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserAccountUpdateTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SecurityTokenUtils securityTokenUtils;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	public Long createUserTestSubject(RegisterDTO registerDTO) throws Exception {
		mockMvc.perform(post("/api/anon/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerDTO))
						.with(csrf()))
				.andExpect(status().isOk());

		User user = userService.findByEmail(registerDTO.email());
		return user.getId();
	}

	@Test
	public void givenValidCurrentPasswordAndNewPassword_whenUpdatingUserPassword_thenUpdateUserPassword() throws Exception {
		// create user test subject

		long testUserId = createUserTestSubject(new RegisterDTO(
				"UserAccountUpdate",
				"UserAccountPasswordUpdate@gmail.com",
				"UserAccountPasswordUpdate@gmail.com",
				"password",
				"password"));

		// create JWT token

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"UserAccountPasswordUpdate@gmail.com",
				testUserId,
				"USER");

		PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO(
				"password",
				"Password1",
				"Password1");

		mockMvc.perform(put("/api/user/{userId}/password", testUserId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(passwordChangeDTO))
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(testUserId), 1800, false, false))
						.with(csrf()))
				.andExpect(status().isOk());

		User user = userService.findByEmail("UserAccountPasswordUpdate@gmail.com");

		assertAll("assert password was successfully updated",
				() -> assertTrue(passwordEncoder.matches(passwordChangeDTO.newPassword(), user.getPassword()))
		);
	}

	// test for ValidateUserAccountUpdate aspect
	@Test
	public void givenInvalidCurrentPassword_whenUpdatingUserPassword_thenThrowBadCredentialsException() throws Exception {
		// create user test subject

		long testUserId = createUserTestSubject(new RegisterDTO(
				"UserAccountUpdate",
				"UserAccountInvalidPasswordUpdate@gmail.com",
				"UserAccountInvalidPasswordUpdate@gmail.com",
				"password",
				"password"));

		// create JWT token

		String validAccessToken = securityTokenUtils.createToken(Instant.now().plus(5, ChronoUnit.MINUTES),
				"test subject",
				testUserId,
				"USER");

		PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO(
				"wrong-current-password",
				"new-password",
				"new-password");

		mockMvc.perform(put("/api/user/{userId}/password", testUserId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(passwordChangeDTO))
						.cookie(SecurityCookieUtils.makeCookie("fight", validAccessToken, 1800, true, false))
						.cookie(SecurityCookieUtils.makeCookie("id", String.valueOf(testUserId), 1800, false, false))
						.with(csrf()))
				.andExpect(result -> MatcherAssert.assertThat(result.getResolvedException(),
						CoreMatchers.instanceOf(BadCredentialsException.class)));
	}
}
