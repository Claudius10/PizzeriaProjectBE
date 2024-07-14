package org.pizzeria.api;

import org.pizzeria.api.entity.role.Role;
import org.pizzeria.api.services.role.RoleService;
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
			Role role = roleService.findByName("USER");
			if (role == null) {
				roleService.create("USER");
			}
		};
	}
}