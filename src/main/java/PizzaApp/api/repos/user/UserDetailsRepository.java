package PizzaApp.api.repos.user;

import PizzaApp.api.entity.user.User;

import java.util.Optional;

public interface UserDetailsRepository {

	Optional<User> findByUsername(String username);
}
