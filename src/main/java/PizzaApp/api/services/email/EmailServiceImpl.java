package PizzaApp.api.services.email;
import org.springframework.stereotype.Service;
import PizzaApp.api.entity.clients.Email;
import PizzaApp.api.repos.email.EmailRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {

	private EmailRepository emailRepository;

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
