package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.dto.user.UserDataDTO;
import PizzaApp.api.entity.user.UserData;
import PizzaApp.api.repos.user.account.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;

	private final PasswordEncoder bCryptEncoder;

	public AccountServiceImpl(AccountRepository userRepository, PasswordEncoder bCryptEncoder) {
		this.accountRepository = userRepository;
		this.bCryptEncoder = bCryptEncoder;
	}

	@Override
	public UserDataDTO findDataById(String id) {
		return accountRepository.findDataById(Long.valueOf(id)).orElseThrow();
	}

	@Override
	public void createData(UserData userData) {
		accountRepository.createData(userData);
	}

	@Override
	public void updateEmail(String id, String email) {
		accountRepository.updateEmail(Long.valueOf(id), email);
	}

	@Override
	public void updatePassword(String id, String password) {
		String encodedPassword = bCryptEncoder.encode(password);
		accountRepository.updatePassword(Long.valueOf(id), encodedPassword);
	}
}
