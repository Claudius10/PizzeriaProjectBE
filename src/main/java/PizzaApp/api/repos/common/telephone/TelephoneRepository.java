package PizzaApp.api.repos.common.telephone;

import PizzaApp.api.entity.common.Telephone;

public interface TelephoneRepository {

	Telephone findByNumber(int tel);
}
