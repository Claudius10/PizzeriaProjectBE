package PizzaApp.api.services.common.email;

import org.springframework.stereotype.Service;
import PizzaApp.api.entity.common.Email;
import PizzaApp.api.repos.common.email.EmailRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {

	private final EmailRepository emailRepository;

	public EmailServiceImpl(EmailRepository emailRepository) {
		this.emailRepository = emailRepository;
	}

	@Override
	public Email findByAddress(Email email) {
		try {
			return emailRepository.findByAddress(email.getEmail());
		} catch (NullPointerException e) {
			return null;
		}
	}
}
