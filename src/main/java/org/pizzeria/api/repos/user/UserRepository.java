package org.pizzeria.api.repos.user;

import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.entity.user.User;
import org.pizzeria.api.web.dto.user.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("select user.addressList from User user where user.id = :userId")
	Set<Address> findUserAddressListById(Long userId);

	Optional<UserDTO> findUserById(Long userId);

	// for internal use only

	@Query("select user from User user join fetch user.roles where user.email = :email")
	Optional<User> findUserByEmailWithRoles(String email);

	@Query("select user from User user left join fetch user.addressList where user.id = :userId")
	Optional<User> findUserByIdWithAddressList(Long userId);

	@Query("select user from User user join fetch user.roles where user.email = :email")
	User findUserByEmail(String email);
}