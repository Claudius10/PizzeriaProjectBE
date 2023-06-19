package PizzaApp.api.services.auth;


import PizzaApp.api.entity.user.dto.AuthDTO;
import PizzaApp.api.entity.user.dto.LoginDTO;
import PizzaApp.api.entity.user.dto.RegisterDTO;

public interface TokenService {

	AuthDTO create(LoginDTO login);

	AuthDTO refresh();
}
