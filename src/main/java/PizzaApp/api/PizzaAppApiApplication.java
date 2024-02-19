package PizzaApp.api;

import PizzaApp.api.entity.role.Role;
import PizzaApp.api.services.role.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class PizzaAppApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzaAppApiApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleService roleService, JdbcTemplate jdbcTemplate) {
		return args -> {
			jdbcTemplate.execute("ALTER TABLE orders DROP FOREIGN KEY FKel9kyl84ego2otj2accfd8mr7");
			jdbcTemplate.execute("ALTER TABLE orders ADD CONSTRAINT FKel9kyl84ego2otj2accfd8mr7 FOREIGN KEY (user_id) references user (id) ON DELETE SET NULL");

			Role role = roleService.findByName("USER");
			if (role == null) {
				roleService.create("USER");
			}
		};
	}
}
