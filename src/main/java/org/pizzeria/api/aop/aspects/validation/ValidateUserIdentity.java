package org.pizzeria.api.aop.aspects.validation;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.pizzeria.api.utils.globals.SecurityResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ValidateUserIdentity {

	// The user id sent as a path variable or request param for endpoints that require it
	// must match the user id stored in the access token.

	@Around(value = "org.pizzeria.api.aop.pointcuts.UserPointCuts.requieresUserIdValidation() && args(.., userId, request)", argNames = "pjp,userId,request")
	public Object validate(ProceedingJoinPoint pjp, Long userId, HttpServletRequest request) throws Throwable {
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(SecurityResponses.USER_ID_MISSING);
		}

		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		Jwt validatedAccessToken = (Jwt) authentication.getPrincipal();

		if (!String.valueOf(userId).equals(validatedAccessToken.getClaimAsString("userId"))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(SecurityResponses.FRAUDULENT_TOKEN);
		}

		return pjp.proceed();
	}
}