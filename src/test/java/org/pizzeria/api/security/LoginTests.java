package org.pizzeria.api.security;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pizzeria.api.entity.dto.auth.RegisterDTO;
import org.pizzeria.api.entity.role.Role;
import org.pizzeria.api.repos.role.RoleRepository;
import org.pizzeria.api.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@DirtiesContext
class LoginTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@BeforeAll
	void setUp() {
		Role role = roleRepository.findByName("USER");
		if (role == null) {
			roleRepository.save(new Role("USER"));
		}
		userService.createUser(new RegisterDTO(
				"tester",
				"test@gmail.com",
				"test@gmail.com",
				"password",
				"password"));
	}

	@Test
	void givenLoginApiCall_whenValidCredentials_thenReturnOk() throws Exception {
		// Act

		// post api call to log in
		mockMvc.perform(post("/api/auth/login?username=test@gmail.com&password=password")
						.with(csrf()))

				// Assert

				.andExpect(status().isOk());
	}

	@Test
	void givenLoginApiCall_whenInvalidCredentials_thenReturnUnauthorized() throws Exception {
		// Act

		// post api call to log in
		mockMvc.perform(post("/api/auth/login?username=void@email.com&password=randomPassword")
						.with(csrf()))

				// Assert

				.andExpect(status().isUnauthorized());
	}

	@Test
	void givenLoginApiCall_whenInvalidPassword_thenReturnUnauthorized() throws Exception {
		// Act

		// post api call to log in
		mockMvc.perform(post("/api/auth/login?username=test@gmail.com&password=wrong_password")
						.with(csrf()))

				// Assert

				.andExpect(status().isUnauthorized());
	}

	@Test
	void givenLoginApiCall_whenInvalidUsername_thenReturnUnauthorized() throws Exception {
		// Act

		// post api call to log in
		mockMvc.perform(post("/api/auth/login?username=nottest@gmail.com&password=password")
						.with(csrf()))

				// Assert

				.andExpect(status().isUnauthorized());
	}
}