package PizzaApp.api.services.token;


import PizzaApp.api.entity.user.Token;
import PizzaApp.api.entity.user.dto.AuthDTO;
import PizzaApp.api.entity.user.dto.LoginDTO;

public interface TokenService {

	AuthDTO create(LoginDTO login);

	AuthDTO refresh(String token);
}
