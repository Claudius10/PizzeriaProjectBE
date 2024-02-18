package PizzaApp.api.repos.resources;

import java.util.List;

import PizzaApp.api.entity.resources.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllByProductType(String type);
}
