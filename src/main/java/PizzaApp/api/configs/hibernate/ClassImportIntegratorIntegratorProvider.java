package PizzaApp.api.configs.hibernate;

import java.util.List;

import PizzaApp.api.entity.dto.order.*;
import PizzaApp.api.entity.dto.user.PasswordDTO;
import PizzaApp.api.entity.dto.user.TelephoneDTO;
import PizzaApp.api.entity.dto.user.UserDataDTO;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;

import io.hypersistence.utils.hibernate.type.util.ClassImportIntegrator;

// Utility that makes possible using the simple Java class name when creating queries for DTO's

public class ClassImportIntegratorIntegratorProvider implements IntegratorProvider {

	@Override
	public List<Integrator> getIntegrators() {
		return List.of(new ClassImportIntegrator(List.of(
				OrderDTO.class,
				OrderSummaryDTO.class,
				OrderPaginationResultDTO.class,
				OrderCreatedOnDTO.class,
				UserDataDTO.class,
				TelephoneDTO.class,
				PasswordDTO.class
		)));
	}
}
