package org.pizzeria.api.configs.hibernate;

import io.hypersistence.utils.hibernate.type.util.ClassImportIntegrator;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;
import org.pizzeria.api.entity.order.dto.OrderDTO;
import org.pizzeria.api.entity.user.dto.PasswordDTO;

import java.util.List;

// Utility that makes possible using the simple Java class name when creating queries for DTO's

public class ClassImportIntegratorIntegratorProvider implements IntegratorProvider {

	@Override
	public List<Integrator> getIntegrators() {
		return List.of(new ClassImportIntegrator(List.of(
				OrderDTO.class,
				PasswordDTO.class
		)));
	}
}
