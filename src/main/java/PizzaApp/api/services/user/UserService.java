package PizzaApp.api.services.user;

import PizzaApp.api.entity.user.dto.RegisterDTO;

public interface UserService {

	void create(RegisterDTO registerDTO);
}
