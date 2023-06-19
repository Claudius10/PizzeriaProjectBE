package PizzaApp.api.configs.hibernate;

import java.util.List;

import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;

import PizzaApp.api.entity.order.dto.OrderCreatedOnDTO;
import PizzaApp.api.entity.order.dto.OrderDTO;
import io.hypersistence.utils.hibernate.type.util.ClassImportIntegrator;

// Utility that makes possible using the simple Java class name when creating queries for DTO's

public class ClassImportIntegratorIntegratorProvider implements IntegratorProvider {

	@Override
	public List<Integrator> getIntegrators() {
		return List.of(new ClassImportIntegrator(List.of(OrderDTO.class, OrderCreatedOnDTO.class)));
	}

}
