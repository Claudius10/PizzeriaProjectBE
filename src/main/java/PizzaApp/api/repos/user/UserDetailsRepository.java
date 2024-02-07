package PizzaApp.api.repos.user;

import PizzaApp.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<User, Long> {

	@Query("select user from User user join fetch user.roles where user.email = :email")
	Optional<User> findByEmail(String email);
}
