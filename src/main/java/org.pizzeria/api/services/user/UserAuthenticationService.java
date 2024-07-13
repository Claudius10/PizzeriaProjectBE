package org.pizzeria.api.services.user;

import jakarta.transaction.Transactional;
import org.pizzeria.api.repos.user.UserRepository;
import org.pizzeria.api.utils.globals.SecurityResponses;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
				.orElseThrow(() -> new UsernameNotFoundException(String.format(SecurityResponses.USER_EMAIL_NOT_FOUND,
						username)));
	}
}