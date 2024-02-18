package PizzaApp.api;

import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.role.Role;
import PizzaApp.api.services.role.RoleService;
import PizzaApp.api.services.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootApplication
public class PizzaAppApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzaAppApiApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleService roleService, UserService userService) {
		return args -> {

			Role role = roleService.findByName("USER");
			if (role == null) {
				roleService.create("USER");
			}

			try {
				userService.findByEmail("clau@gmail.com");
			} catch (UsernameNotFoundException ex) {
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
