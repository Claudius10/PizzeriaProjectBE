package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.dto.user.EmailChangeDTO;
import PizzaApp.api.entity.dto.user.NameChangeDTO;
import PizzaApp.api.entity.dto.user.PasswordChangeDTO;
import PizzaApp.api.entity.dto.user.PasswordDTO;
import PizzaApp.api.exceptions.exceptions.user.InvalidPasswordException;
import PizzaApp.api.repos.user.account.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder bCryptEncoder;

	public UserServiceImpl
			(UserRepository userRepository,
			 PasswordEncoder bCryptEncoder) {
		this.userRepository = userRepository;
		this.bCryptEncoder = bCryptEncoder;
	}

	@Override
	public void updateName(String id, NameChangeDTO nameChangeDTO) {
		verifyPassword(id, nameChangeDTO.password());
		userRepository.updateName(id, nameChangeDTO.name());
	}

	@Override
	public void updateEmail(String id, EmailChangeDTO emailChangeDTO) {
		verifyPassword(id, emailChangeDTO.password());
		userRepository.updateEmail(id, emailChangeDTO.email());
	}

	@Override
	public void updatePassword(String id, PasswordChangeDTO passwordChangeDTO) {
		verifyPassword(id, passwordChangeDTO.currentPassword());
		userRepository.updatePassword(id, passwordChangeDTO.newPassword());
	}

	@Override
	public void delete(String id, PasswordDTO passwordDTO) {
		verifyPassword(id, passwordDTO.password());
		userRepository.delete(id);
	}

	@Override
	public String loadPassword(String id) {
		return userRepository.loadPassword(id);
	}

	private void verifyPassword(String id, String password) {
		if (!bCryptEncoder.matches(password, loadPassword(id))) {
			throw new InvalidPasswordException("La contraseña introducida no coincide con la contraseña almacenada");
		}
	}
}
