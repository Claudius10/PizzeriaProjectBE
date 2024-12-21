package org.pizzeria.api.repos;

import org.junit.jupiter.api.Test;
import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.repos.address.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
class AddressRepositoryTests {

	@Autowired
	private AddressRepository addressRepository;

	@Test
	void givenAddress_thenCreateAddress() {
		// Arrange

		Address testAddress = new Address.Builder()
				.withStreet("Test Street")
				.withNumber(9)
				.build();

		// Act

		addressRepository.save(testAddress);
		long addressCount = addressRepository.count();

		// Assert
		assertThat(addressCount).isEqualTo(1);
	}
}