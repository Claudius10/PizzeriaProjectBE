package PizzaApp.api.repos.telephone;
import PizzaApp.api.entity.clients.Telephone;

public interface TelephoneRepository {

	public Telephone findByNumber(int tel);
}
