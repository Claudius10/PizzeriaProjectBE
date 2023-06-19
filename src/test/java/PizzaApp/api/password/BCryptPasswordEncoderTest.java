package PizzaApp.api.password;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BCryptPasswordEncoderTest {

	@Test
	public void givenRawPassword_whenEncodingPassword_TakeMin1SecToVerifyStrength() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(14);
		String result = encoder.encode("myPassword");
		assertTrue(encoder.matches("myPassword", result));
	}
}
