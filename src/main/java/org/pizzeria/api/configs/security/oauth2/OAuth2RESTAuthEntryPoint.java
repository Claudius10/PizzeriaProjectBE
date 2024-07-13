package org.pizzeria.api.configs.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class OAuth2RESTAuthEntryPoint implements AuthenticationEntryPoint {

	private final HandlerExceptionResolver resolver;

	public OAuth2RESTAuthEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public void commence(HttpServletRequest request,
						 HttpServletResponse response,
						 AuthenticationException authException) {
		// return an empty ModelAndView so ex can be picked up by @ControllerAdvice
		resolver.resolveException(request, response, null, authException);
	}
}
