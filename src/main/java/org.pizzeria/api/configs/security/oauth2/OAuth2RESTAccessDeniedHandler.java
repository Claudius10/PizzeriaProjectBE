package org.pizzeria.api.configs.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class OAuth2RESTAccessDeniedHandler implements AccessDeniedHandler {

	private final HandlerExceptionResolver resolver;

	public OAuth2RESTAccessDeniedHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public void handle(HttpServletRequest request,
					   HttpServletResponse response,
					   AccessDeniedException accessDeniedException) {
		// return an empty ModelAndView so ex can be picked up by @ControllerAdvice
		resolver.resolveException(request, response, null, accessDeniedException);
	}
}
