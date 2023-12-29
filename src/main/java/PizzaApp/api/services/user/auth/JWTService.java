package PizzaApp.api.services.user.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public interface JWTService {

	String refreshTokens(HttpServletResponse response, Cookie token);
}
