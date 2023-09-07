package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.dto.misc.RegisterDTO;

public interface AnonUserService {

	void create(RegisterDTO registerDTO);
}
