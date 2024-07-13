package org.pizzeria.api.repos.resources;

import org.pizzeria.api.entity.resources.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllByProductType(String type);
}