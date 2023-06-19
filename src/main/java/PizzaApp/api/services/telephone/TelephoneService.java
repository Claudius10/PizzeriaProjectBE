package PizzaApp.api.services.telephone;

import PizzaApp.api.entity.user.common.Telephone;

public interface TelephoneService {

	Telephone findByNumber(Telephone telephone);
}
