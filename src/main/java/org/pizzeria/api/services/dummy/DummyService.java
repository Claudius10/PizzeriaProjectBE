package org.pizzeria.api.services.dummy;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.entity.cart.Cart;
import org.pizzeria.api.entity.cart.CartItem;
import org.pizzeria.api.entity.order.OrderDetails;
import org.pizzeria.api.entity.role.Role;
import org.pizzeria.api.entity.user.User;
import org.pizzeria.api.repos.user.UserRepository;
import org.pizzeria.api.services.address.AddressService;
import org.pizzeria.api.services.order.OrderService;
import org.pizzeria.api.services.role.RoleService;
import org.pizzeria.api.services.user.UserService;
import org.pizzeria.api.utils.Constants;
import org.pizzeria.api.web.dto.auth.RegisterDTO;
import org.pizzeria.api.web.dto.order.dto.NewUserOrderDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Profile("production")
@Slf4j
public class DummyService {

	private final UserService userService;

	private final UserRepository userRepository;

	private final OrderService orderService;

	private final RoleService roleService;

	private final AddressService addressService;

	@PostConstruct
	public void init() {
		setupRoles();
		setupDummyUser();
	}

	private void setupRoles() {
		if (roleService.findByName("USER").isEmpty()) {
			this.roleService.createRole(new Role("USER"));
		}

		if (roleService.findByName("ADMIN").isEmpty()) {
			this.roleService.createRole(new Role("ADMIN"));
		}
	}

	private void setupDummyUser() {
		if (!exists()) {
			createDummyUser();
			User user = userRepository.findUserByEmail(Constants.DUMMY_ACCOUNT_EMAIL);
			log.info("Dummy user setup: id is {}", user.getId());
			addAddress(user.getId());
			addOrder(user.getId());
			log.info("Dummy user setup: done");
		} else {
			log.info("Dummy user setup: nothing to do");
		}
	}

	private boolean exists() {
		User it = new User();
		it.setEmail(Constants.DUMMY_ACCOUNT_EMAIL);
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withIgnoreNullValues()
				.withStringMatcher(ExampleMatcher.StringMatcher.EXACT);

		Example<User> example = Example.of(it, matcher);
		return userRepository.exists(example);
	}

	private void createDummyUser() {
		log.info("Creating dummy user");
		userService.createUser(new RegisterDTO(
				"Miguel de Cervantes",
				Constants.DUMMY_ACCOUNT_EMAIL,
				Constants.DUMMY_ACCOUNT_EMAIL,
				123456789,
				"Password1",
				"Password1"
		));
	}

	private void addAddress(Long userId) {
		log.info("Adding address to dummy user");
		userService.addUserAddress(userId, Address.builder()
				.withStreet("En un lugar de la Mancha...")
				.withNumber(1605)
				.build()
		);
	}

	private void addOrder(Long userId) {
		log.info("Adding order to dummy user");

		Address address = Address.builder()
				.withStreet("En un lugar de la Mancha...")
				.withNumber(1605)
				.build();

		Optional<Address> addressByExample = addressService.findByExample(address);
		log.info("Dummy user's address exists {}", addressByExample.isPresent());

		NewUserOrderDTO order = new NewUserOrderDTO(
				addressByExample.get().getId(),
				OrderDetails.builder()
						.withDeliveryTime("form.select.time.asap")
						.withPaymentMethod("form.select.payment.method.cash")
						.build(),
				new Cart.Builder()
						.withTotalQuantity(1)
						.withTotalCost(13.30D)
						.withCartItems(List.of(CartItem.builder()
								.withCode("P4C")
								.withFormat("M")
								.withPrice(13.30)
								.withQuantity(1)
								.build()))
						.build()
		);

		orderService.createUserOrder(userId, order);
	}
}