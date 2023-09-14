package PizzaApp.api;

import PizzaApp.api.entity.user.Role;
import PizzaApp.api.services.user.role.RoleService;
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
			Role admin = roleService.findByName("ADMIN");
			if (!admin.getAuthority().isEmpty()) return;
			roleService.create("ADMIN");
			roleService.create("USER");
		};
	}
}
