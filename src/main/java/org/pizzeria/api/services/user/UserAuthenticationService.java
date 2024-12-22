package org.pizzeria.api.services.user;

import jakarta.transaction.Transactional;
import org.pizzeria.api.repos.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.pizzeria.api.web.globals.SecurityResponses.USER_NOT_FOUND;

@Service
@Transactional
public class UserAuthenticationService implements UserDetailsService {

	private final UserRepository userRepository;

	public UserAuthenticationService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findUserByEmailWithRoles(username)
				.orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND)); // this ends up as AuthenticationException
	}
}