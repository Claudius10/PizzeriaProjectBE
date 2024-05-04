package PizzaApp.api.aop.aspects.validation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ValidateUserAccountUpdate {

	final private AuthenticationManager authenticationManager;

	public ValidateUserAccountUpdate(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Before(value = "PizzaApp.api.aop.pointcuts.UserPointCuts.isUserAccountUpdate() && args(password, ..)", argNames =
			"password")
	public void verifyPassword(String password) {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		Jwt jwt = (Jwt) authentication.getPrincipal();
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwt.getSubject(), password));
	}
}