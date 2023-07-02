package PizzaApp.api.services.common.telephone;

import PizzaApp.api.entity.common.Telephone;

public interface TelephoneService {

	Telephone findByNumber(Telephone telephone);
}
