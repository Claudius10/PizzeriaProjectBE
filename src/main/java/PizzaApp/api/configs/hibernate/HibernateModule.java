package PizzaApp.api.configs.hibernate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;

@Configuration
public class HibernateModule {

	@Bean
	Hibernate6Module datatypeHibernateModule() {
		return new Hibernate6Module();
	}
}