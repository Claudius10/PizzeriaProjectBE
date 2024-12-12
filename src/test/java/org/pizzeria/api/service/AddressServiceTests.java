package org.pizzeria.api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.repos.address.AddressRepository;
import org.pizzeria.api.services.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
class AddressServiceTests {

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private AddressService addressService;

	@Test
	void givenAddress_thenCreateAddress() {
		// Arrange

		Address testAddress = new Address.Builder()
				.withStreet("Test Street")
				.withNumber(9)
				.build();

		// Act

		addressService.create(testAddress);
		long addressCount = addressRepository.count();

		// Assert
		assertThat(addressCount).isEqualTo(1);
	}

	@Test
	void givenAddress_whenExample_thenFindAddress() {
		// Arrange

		Address testAddress = new Address.Builder()
				.withStreet("Test Street")
				.withNumber(9)
				.build();

		// Act

		addressService.create(testAddress);
		Optional<Address> address = addressService.findByExample(testAddress);

		// Assert
		assertThat(address.get().getStreet()).isEqualTo(testAddress.getStreet());
	}

	@Test
	void givenAddressId_thenFindAddressById() {
		// Arrange

		Address testAddress = new Address.Builder()
				.withStreet("Test Street")
				.withNumber(9)
				.build();

		// Act

		Long addressId = addressService.create(testAddress);
		Optional<Address> address = addressService.findAddressById(addressId);

		// Assert
		assertThat(address.get().getId()).isEqualTo(addressId);
	}
}