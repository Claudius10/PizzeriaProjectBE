package PizzaApp.api.services.user;

import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.entity.user.dto.UserDTO;

import java.util.Optional;
import java.util.Set;

public interface UserService {

	Long createUser(RegisterDTO registerDTO);

	Set<Address> findUserAddressListById(Long userId);

	String addUserAddress(Long userId, Address address);

	String removeUserAddress(Long userId, Long addressId);

	User findUserReference(Long userId);

	Optional<UserDTO> findUserDTOById(Long userId);

	void updateUserName(String password, Long userId, String name);

	void updateUserEmail(String password, Long userId, String email);

	void updateUserContactNumber(String password, Long userId, Integer contactNumber);

	void updateUserPassword(String password, Long userId, String newPassword);

	void deleteUserById(String password, Long userId);

	// for internal use only

	boolean existsById(Long userId);

	User findUserByEmail(String userEmail);

	Optional<User> findUserByIdWithAddressList(Long userId);
}