package PizzaApp.api;

import PizzaApp.api.entity.dto.misc.RegisterDTO;
import PizzaApp.api.services.user.account.UserService;
import PizzaApp.api.services.user.role.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;

@SpringBootApplication
public class PizzaAppApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzaAppApiApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleService roleService, UserService userService) {
		return args -> {
			try {
				roleService.findByName("USER");
			} catch (EmptyResultDataAccessException ex) {
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
