package PizzaApp.api.configs.security;

import PizzaApp.api.configs.security.auth.EatAllCookies;
import PizzaApp.api.configs.security.oauth2.OAuth2RESTAccessDeniedHandler;
import PizzaApp.api.configs.security.oauth2.OAuth2RESTAuthEntryPoint;
import PizzaApp.api.configs.security.auth.InvalidLoginHandler;
import PizzaApp.api.configs.security.auth.ValidLoginHandler;
import PizzaApp.api.configs.security.utils.JWTUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.*;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

	private final JWTUtils nimbusJWT;

	private final ValidLoginHandler validAuthHandler;

	private final InvalidLoginHandler invalidAuthHandler;

	private final EatAllCookies eatAllCookies;

	private final CookieBearerTokenResolver cookieBearerTokenResolver;

	private final OAuth2RESTAuthEntryPoint oAuth2RESTAuthEntryPoint;

	private final OAuth2RESTAccessDeniedHandler oAuth2RESTAccessDeniedHandler;

	public SecurityConfig
			(JWTUtils nimbusJWT,
			 ValidLoginHandler validAuthHandler,
			 InvalidLoginHandler invalidAuthHandler,
			 EatAllCookies eatAllCookies, CookieBearerTokenResolver cookieBearerTokenResolver,
			 OAuth2RESTAuthEntryPoint oAuth2RESTAuthEntryPoint,
			 OAuth2RESTAccessDeniedHandler oAuth2RESTAccessDeniedHandler) {
		this.nimbusJWT = nimbusJWT;
		this.validAuthHandler = validAuthHandler;
		this.invalidAuthHandler = invalidAuthHandler;
		this.eatAllCookies = eatAllCookies;
		this.cookieBearerTokenResolver = cookieBearerTokenResolver;
		this.oAuth2RESTAuthEntryPoint = oAuth2RESTAuthEntryPoint;
		this.oAuth2RESTAccessDeniedHandler = oAuth2RESTAccessDeniedHandler;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.cors(withDefaults());
		http.csrf(csrf -> {

			// persist CSRF token in a cookie
			csrf.csrfTokenRepository(csrfTokenRepository());

			// resolve CSRF token off the x-xsrf-token HTTP request header
			csrf.csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler());

			// https://github.com/spring-projects/spring-security/issues/8668 for
			// enabling CSRF token protection for post auth requests with JWT tokens
			// with a custom ant matcher (csrfProtectionMatcher bean)
			csrf.withObjectPostProcessor(new ObjectPostProcessor<CsrfFilter>() {
				@Override
				public <O extends CsrfFilter> O postProcess(O object) {
					object.setRequireCsrfProtectionMatcher(csrfProtectionMatcher());
					return object;
				}
			});
		});

		// post authentication JWT resource protection
		http.oauth2ResourceServer(oauth2ResourceServer -> {
			oauth2ResourceServer.jwt(jwt -> {
				jwt.decoder(nimbusJWT.jwtDecoder());
				jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
			});
			// customizes how jwt authentication failures are handled
			oauth2ResourceServer.authenticationEntryPoint(oAuth2RESTAuthEntryPoint);
			// customizes how jwt access denied errors are handled
			oauth2ResourceServer.accessDeniedHandler(oAuth2RESTAccessDeniedHandler);
			// load JWT from cookie instead of Authorization header
			oauth2ResourceServer.bearerTokenResolver(cookieBearerTokenResolver);
		});

		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers("/api/resource/**").permitAll();
			auth.requestMatchers("/api/anon/**").permitAll();
			auth.requestMatchers("/api/token/**").permitAll();
			auth.requestMatchers("/api/user/**").hasAnyRole("USER");
			auth.requestMatchers("/api/tests/**").hasRole("USER");
			auth.anyRequest().authenticated();
		});

		http.formLogin(formLogin -> {
			formLogin.loginPage("/api/auth/login");
			formLogin.successHandler(validAuthHandler);
			formLogin.failureHandler(invalidAuthHandler);
		});

		http.logout(logout -> {
			logout.logoutUrl("/api/auth/logout");
			logout.addLogoutHandler(eatAllCookies);
			// return 200 on successful logout
			logout.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
		});

		return http.build();
	}

	/*
	Set Access-Control-Allow-Headers when allowing headers to be passed from the client to the server (e.g. If-Match).
	Set Access-Control-Expose-Headers when allowing headers to be passed back from the server to the client (e.g. ETag).
	*/

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(
				Arrays.asList("http://192.168.0.10:3000", "https://pizzeriaproject-production.up.railway.app"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		configuration.setExposedHeaders(Arrays.asList("Content-Type", "x-xsrf-token"));
		configuration.setAllowedHeaders(Arrays.asList("Content-Type", "x-xsrf-token"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(6);
	}

	@Bean
	public AuthenticationManager authManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(authenticationProvider);
	}

	private RequestMatcher csrfProtectionMatcher() {
		// custom CsrfFilter protection matcher implementation
		AntPathRequestMatcher[] noCsrfTokenRoutes = {
				new AntPathRequestMatcher("/api/resource/**"),
				new AntPathRequestMatcher("/api/anon/**")
		};

		return request -> {
			for (AntPathRequestMatcher rm : noCsrfTokenRoutes) {
				if (rm.matches(request)) {
					return false;
				}
			}

			// default CsrfFilter protection matcher behavior
			Set<String> allowedMethods = new HashSet<>(Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));
			return !allowedMethods.contains(request.getMethod());
		};
	}

	private CookieCsrfTokenRepository csrfTokenRepository() {
		CookieCsrfTokenRepository result = new CookieCsrfTokenRepository();
		result.setCookieCustomizer((cookie) -> {
			cookie.httpOnly(false);
			cookie.secure(true); // NOTE - on for prod fe
			cookie.domain(".up.railway.app"); // NOTE - on for prod fe
		});
		return result;
	}

	// change the auto added prefix to roles from SCOPE_ to ROLE_
	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
		JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
		jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
		return jwtConverter;
	}
}