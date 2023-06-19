package PizzaApp.api;

import PizzaApp.api.services.role.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PizzaAppApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzaAppApiApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleService roleService) {
		return args -> {
			if (roleService.findByName("ADMIN").isPresent()) return;
			roleService.create("ADMIN");
			roleService.create("USER");
		};
	}
}
