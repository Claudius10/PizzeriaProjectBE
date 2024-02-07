package PizzaApp.api.aop.validation;

import PizzaApp.api.repos.user.UserRepository;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class BeforeAccountUpdate {

	final private AuthenticationManager authenticationManager;

	private final UserRepository userRepository;

	public BeforeAccountUpdate(AuthenticationManager authenticationManager, UserRepository userRepository) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
	}

	@Before(value = "execution(* PizzaApp.api.services.user.UserService.update*(..)) && args(password, userId, ..)", argNames = "password,userId")
	public void verifyPassword(String password, Long userId) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRepository.findUserEmailById(userId), password));
	}
}
