package PizzaApp.api;

import PizzaApp.api.entity.dto.misc.RegisterDTO;
import PizzaApp.api.entity.user.Role;
import PizzaApp.api.services.user.account.UserService;
import PizzaApp.api.services.user.role.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class PizzaAppApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzaAppApiApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleService roleService, UserService userService) {
		return args -> {
			// create user role and dummy account if it doesn't exist
			Optional<Role> userRole = roleService.findByName("USER");
			if (userRole.isEmpty()) {
				roleService.create("USER");
				userService.create(new RegisterDTO(
						"Clau",
						"clau@gmail.com",
						"clau@gmail.com",
						"password",
						"password"));
			}
		};
	}
}
