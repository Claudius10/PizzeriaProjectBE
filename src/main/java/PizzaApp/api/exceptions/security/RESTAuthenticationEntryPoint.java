package PizzaApp.api.exceptions.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class RESTAuthenticationEntryPoint implements AuthenticationEntryPoint {

	/**
	 * {@link ExceptionTranslationFilter} delegates the handling of {@link AuthenticationException}
	 * and its subclasses (very important) to implementations of {@link AuthenticationEntryPoint}.
	 * This happens during the SecurityFilterChain's execution, before invoking any MVC controllers.
	 * {@link HandlerExceptionResolver} passes these exceptions as ModelAndView and when
	 * handling them with @ExceptionHandler, an APIErrorDTO can be returned in controllers.
	 */

	private final HandlerExceptionResolver resolver;

	public RESTAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public void commence(HttpServletRequest request,
						 HttpServletResponse response,
						 AuthenticationException authException) {
		// pass the exception to handle it with @ExceptionHandler
		resolver.resolveException(request, response, null, authException);
	}
}
