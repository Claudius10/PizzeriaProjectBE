package PizzaApp.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PizzaAppApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzaAppApiApplication.class, args);
	}

/*
	// NOTE - comment out when testing
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
	}*/
}