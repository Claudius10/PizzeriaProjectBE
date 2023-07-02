package PizzaApp.api.services.user.account;

import PizzaApp.api.entity.dto.misc.RegisterDTO;

public interface AnonAccService {

	void create(RegisterDTO registerDTO);
}
