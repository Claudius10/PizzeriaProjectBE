package PizzaApp.api.services.user.auth;

import PizzaApp.api.entity.dto.misc.AuthDTO;
import PizzaApp.api.entity.dto.misc.LoginDTO;
import jakarta.servlet.http.Cookie;

public interface AuthService {

	AuthDTO login(LoginDTO login);

	AuthDTO refreshTokens(Cookie token);
}
