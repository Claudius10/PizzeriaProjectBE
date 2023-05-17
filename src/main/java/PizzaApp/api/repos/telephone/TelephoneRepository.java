package PizzaApp.api.repos.telephone;
import java.util.List;
import PizzaApp.api.entity.clients.customer.Telephone;

public interface TelephoneRepository {

	public Telephone findByNumber(Telephone telephone);

	public List<Telephone> findAllByNumber(Telephone telephone);
}
