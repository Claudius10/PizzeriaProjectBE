package PizzaApp.api.repos.telephone;

import PizzaApp.api.entity.user.common.Telephone;

public interface TelephoneRepository {

	Telephone findByNumber(int tel);
}
