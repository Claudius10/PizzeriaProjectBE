package PizzaApp.api.configs.security;

import PizzaApp.api.exceptions.security.RESTAccessDeniedHandler;
import PizzaApp.api.exceptions.security.RESTAuthenticationEntryPoint;
import PizzaApp.api.utility.jwt.keys.RSAKeyPair;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
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
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
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

	private final RSAKeyPair keys;
	// return APIErrorDTOs based of AuthenticationExceptions
	private final RESTAuthenticationEntryPoint restAuthenticationEntryPoint;
	// return APIErrorDTOs based of AccessDeniedExceptions
	private final RESTAccessDeniedHandler restAccessDeniedHandler;
	private final CookieBearerTokenResolver cookieBearerTokenResolver;

	public SecurityConfig(RSAKeyPair keys,
						  RESTAuthenticationEntryPoint restAuthenticationEntryPoint,
						  RESTAccessDeniedHandler restAccessDeniedHandler,
						  CookieBearerTokenResolver cookieBearerTokenResolver) {
		this.keys = keys;
		this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
		this.restAccessDeniedHandler = restAccessDeniedHandler;
		this.cookieBearerTokenResolver = cookieBearerTokenResolver;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// cors config
		http.cors(withDefaults());

		// CSRF config
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

		// JWT support config
		http.oauth2ResourceServer(oauth2ResourceServer -> {
			oauth2ResourceServer.jwt(jwt -> {
				jwt.decoder(jwtDecoder());
				jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
			});
			oauth2ResourceServer.authenticationEntryPoint(restAuthenticationEntryPoint);
			oauth2ResourceServer.accessDeniedHandler(restAccessDeniedHandler);
			// load JWT from cookie instead of Authorization header
			oauth2ResourceServer.bearerTokenResolver(cookieBearerTokenResolver);
		});

		// endpoints config
		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers("/api/auth/**").permitAll();
			auth.requestMatchers("/api/resource/**").permitAll();
			auth.requestMatchers("/api/order/**").permitAll();
			auth.requestMatchers("/api/account/**").hasAnyRole("USER", "ADMIN");
			auth.requestMatchers("/api/admin/**").hasRole("ADMIN");
			auth.anyRequest().authenticated();
		});

		http.sessionManagement(session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

	@Bean
	RequestMatcher csrfProtectionMatcher() {
		// custom CsrfFilter protection matcher implementation
		AntPathRequestMatcher[] requestMatchers = {
				new AntPathRequestMatcher("/api/order/**"),
				new AntPathRequestMatcher(("/api/resource/**"))
		};

		return request -> {
			for (AntPathRequestMatcher rm : requestMatchers) {
				if (rm.matches(request)) {
					return false;
				}
			}

			// default CsrfFilter protection matcher behavior
			Set<String> allowedMethods = new HashSet<>(Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));
			return !allowedMethods.contains(request.getMethod());
		};
	}

	/*
	Set Access-Control-Allow-Headers when allowing headers to be passed from the client to the server (e.g. If-Match).
	Set Access-Control-Expose-Headers when allowing headers to be passed back from the server to the client (e.g. ETag).
	*/

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(
				Arrays.asList("http://192.168.1.11:3000", "https://pizzeriaproject-production.up.railway.app"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		configuration.setExposedHeaders(Arrays.asList("Content-Type", "x-xsrf-token"));
		configuration.setAllowedHeaders(Arrays.asList("Content-Type", "x-xsrf-token"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	CookieCsrfTokenRepository csrfTokenRepository() {
		CookieCsrfTokenRepository result = new CookieCsrfTokenRepository();
		result.setCookieCustomizer((cookie) -> {
			cookie.httpOnly(false);
			//cookie.secure(true); // for prod fe
			//cookie.domain(".up.railway.app"); // this domain for prod fe
		});
		return result;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authManager(UserDetailsService userDetailsService) {
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setUserDetailsService(userDetailsService);
		daoProvider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(daoProvider);
	}

	@Bean
	public JwtEncoder jwtEncoder() {
		JWK jwk = new RSAKey.Builder(keys.getPublicKey()).privateKey(keys.getPrivateKey()).build();
		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
		decoder.setJwtValidator(JwtValidators
				.createDefaultWithIssuer("https://pizzeriaprojectbe-production.up.railway.app"));
		return decoder;
	}

	// change the auto added prefix to roles from SCOPE_ to ROLE_
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
		JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
		jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
		return jwtConverter;
	}
}