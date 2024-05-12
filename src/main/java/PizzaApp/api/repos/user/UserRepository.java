package PizzaApp.api.repos.user;

import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.entity.user.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("select user.addressList from User user where user.id = :userId")
	Set<Address> findUserAddressListById(Long userId);

	Optional<UserDTO> findUserById(Long userId);

	@Modifying
	@Query("update User user set user.name = :name where user.id = :userId")
	void updateUserName(Long userId, String name);

	@Modifying
	@Query("update User user set user.email = :email where user.id = :userId")
	void updateUserEmail(Long userId, String email);

	@Modifying
	@Query("update User user set user.contactNumber = :contactNumber where user.id = :userId")
	void updateUserContactNumber(Long userId, Integer contactNumber);

	@Modifying
	@Query("update User user set user.password = :password where user.id = :userId")
	void updateUserPassword(Long userId, String password);

	// for internal use only

	@Query("select user from User user join fetch user.roles where user.email = :email")
	Optional<User> findUserByEmailWithRoles(String email);

	@Query("select user from User user left join fetch user.addressList where user.id = :userId")
	Optional<User> findUserByIdWithAddressList(Long userId);

	User findUserByEmail(String email);
}