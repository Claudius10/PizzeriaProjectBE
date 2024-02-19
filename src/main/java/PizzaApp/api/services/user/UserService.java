package PizzaApp.api.services.user;

import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.dto.auth.RegisterDTO;
import PizzaApp.api.entity.user.User;
import PizzaApp.api.repos.user.projections.UserProjection;

import java.util.Optional;
import java.util.Set;

public interface UserService {

	Long create(RegisterDTO registerDTO);

	Set<Address> findAddressListById(Long userId);

	boolean addAddress(Long userId, Address address);

	void removeAddress(Long userId, Long addressId);

	Optional<User> findById(Long userId);

	User findReference(Long userId);

	UserProjection findDTOById(Long userId);

	void updateName(String password, Long userId, String name);

	void updateEmail(String password, Long userId, String email);

	void updateUserContactNumber(String password, Long userId, Integer contactNumber);

	void updatePassword(String password, Long userId, String newPassword);

	void updateDelete(String password, Long userId);

	// for internal use only

	User findByEmail(String userEmail);

	User findByIdWithAddressList(Long userId);
}

