package PizzaApp.api.configs.security;

import PizzaApp.api.exceptions.security.RESTAccessDeniedHandler;
import PizzaApp.api.exceptions.security.RESTAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	// return APIErrorDTOs based of AuthenticationExceptions
	private final RESTAuthenticationEntryPoint restAuthenticationEntryPoint;

	// return APIErrorDTOs based of AccessDeniedExceptions
	private final RESTAccessDeniedHandler restAccessDeniedHandler;

	public SecurityConfig(RESTAuthenticationEntryPoint restAuthenticationEntryPoint,
						  RESTAccessDeniedHandler restAccessDeniedHandler) {
		this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
		this.restAccessDeniedHandler = restAccessDeniedHandler;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		// CSRF protection disabled for the time being
		http.csrf(AbstractHttpConfigurer::disable);

		// configure endpoints security
		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers("/api/auth/**").permitAll();
			auth.anyRequest().authenticated();
		});

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(14);
	}
}
