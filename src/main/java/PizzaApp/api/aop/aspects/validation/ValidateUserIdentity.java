package PizzaApp.api.aop.aspects.validation;

import PizzaApp.api.utils.globals.SecurityResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
@Aspect
public class ValidateUserIdentity {

	// The user id sent as a path variable or request param for endpoints that require it
	// must match the user id stored in the access token.

	@Around(value = "PizzaApp.api.aop.pointcuts.UserPointCuts.requieresUserIdValidation() && args(.., request)")
	public Object validate(ProceedingJoinPoint pjp, HttpServletRequest request) throws Throwable {
		Cookie userIdCookie = WebUtils.getCookie(request, "id");

		if (userIdCookie == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(SecurityResponses.USER_ID_MISSING);
		}

		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		Jwt validatedAccessToken = (Jwt) authentication.getPrincipal();

		if (!userIdCookie.getValue().matches(validatedAccessToken.getClaimAsString("userId"))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(SecurityResponses.FRAUDULENT_TOKEN);
		}

		return pjp.proceed();
	}
}