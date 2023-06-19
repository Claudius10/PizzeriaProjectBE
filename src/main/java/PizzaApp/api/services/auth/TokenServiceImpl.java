package PizzaApp.api.services.auth;

import PizzaApp.api.entity.user.dto.AuthDTO;
import PizzaApp.api.entity.user.dto.LoginDTO;
import PizzaApp.api.entity.user.dto.RegisterDTO;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

	private final UserDetailsService userDetailsService;

	public TokenServiceImpl(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	public AuthDTO create(LoginDTO login) {
		return null;
	}

	@Override
	public AuthDTO refresh() {
		return null;
	}
}
