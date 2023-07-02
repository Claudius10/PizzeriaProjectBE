package PizzaApp.api.repos.user.userdetails;

import PizzaApp.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
}
