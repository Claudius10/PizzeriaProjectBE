package PizzaApp.api.exceptions.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class RESTAccessDeniedHandler implements AccessDeniedHandler {

	/**
	 * {@link ExceptionTranslationFilter} delegates the handling of {@link AccessDeniedException}
	 * and its subclasses (very important) to implementations of {@link AccessDeniedHandler}.
	 * This happens during the SecurityFilterChain's execution, before invoking any MVC controllers.
	 * {@link HandlerExceptionResolver} is able to pass these exceptions to the class
	 * annotated with @ControllerAdvice or @RestControllerAdvice and handle them
	 * with @ExceptionHandler annotated methods, thus returning APIErrorDTOs in controllers.
	 */

	private final HandlerExceptionResolver resolver;

	public RESTAccessDeniedHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public void handle(HttpServletRequest request,
					   HttpServletResponse response,
					   AccessDeniedException accessDeniedException) {

		if (resolver.resolveException(request, response, null, accessDeniedException) == null) {
			throw accessDeniedException;
		} else {
			// pass the exception to handle it with @ExceptionHandler
			resolver.resolveException(request, response, null, accessDeniedException);
		}
	}
}
