package org.pizzeria.api.services.user;

import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.entity.user.User;
import org.pizzeria.api.web.dto.auth.RegisterDTO;
import org.pizzeria.api.web.dto.user.dto.UserDTO;

import java.util.Optional;
import java.util.Set;

public interface UserService {

	void createUser(RegisterDTO registerDTO);

	Set<Address> findUserAddressListById(Long userId);

	boolean addUserAddress(Long userId, Address address);

	boolean removeUserAddress(Long userId, Long addressId);

	User findUserReference(Long userId);

	Optional<UserDTO> findUserDTOById(Long userId);

	void updateUserName(String password, Long userId, String name);

	void updateUserEmail(String password, Long userId, String email);

	void updateUserContactNumber(String password, Long userId, Integer contactNumber);

	void updateUserPassword(String password, Long userId, String newPassword);

	boolean deleteUserById(String password, Long userId);

	User findUserOrThrow(Long userId);

	// for internal use only

	boolean existsById(Long userId);
}