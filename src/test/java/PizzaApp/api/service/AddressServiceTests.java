package PizzaApp.api.service;

import PizzaApp.api.entity.address.Address;
import PizzaApp.api.repos.address.AddressRepository;
import PizzaApp.api.services.address.AddressService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
public class AddressServiceTests {

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private AddressService addressService;

	@Test
	public void givenAddress_thenCreateAddress() {
		// Arrange

		Address testAddress = new Address.Builder()
				.withStreet("Test Street")
				.withStreetNr(9)
				.build();

		// Act

		addressService.create(testAddress);
		long addressCount = addressRepository.count();

		// Assert
		assertThat(addressCount).isEqualTo(1);
	}

	@Test
	public void givenAddress_whenExample_thenFindAddress() {
		// Arrange

		Address testAddress = new Address.Builder()
				.withStreet("Test Street")
				.withStreetNr(9)
				.build();

		// Act

		addressService.create(testAddress);
		Optional<Address> address = addressService.findByExample(testAddress);

		// Assert
		assertThat(address.get().getStreet()).isEqualTo(testAddress.getStreet());
	}

	@Test
	public void givenAddressId_thenFindAddressById() {
		// Arrange

		Address testAddress = new Address.Builder()
				.withStreet("Test Street")
				.withStreetNr(9)
				.build();

		// Act

		Long addressId = addressService.create(testAddress);
		Optional<Address> address = addressService.findAddressById(addressId);

		// Assert
		assertThat(address.get().getId()).isEqualTo(addressId);
	}
}