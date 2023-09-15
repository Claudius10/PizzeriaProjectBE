package PizzaApp.api.utility.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class JWTUtils {

	private final JwtEncoder jwtEncoder;
	private final JwtDecoder jwtDecoder;

	public JWTUtils(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
		this.jwtEncoder = jwtEncoder;
		this.jwtDecoder = jwtDecoder;
	}

	public String createToken(Instant expiry, String username, Long userId, String roles) {
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("https://pizzeriaprojectbe-production.up.railway.app") // this domain for prod fe
				.issuedAt(Instant.now())
				.expiresAt(expiry)
				.subject(username)
				.claim("id", userId)
				.claim("roles", roles)
				.build();
		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}

	public Jwt validate(String refreshToken) {
		return jwtDecoder.decode(refreshToken);
	}

	public String parseRoles(Collection<? extends GrantedAuthority> authorities) {
		return authorities.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));
	}
}
