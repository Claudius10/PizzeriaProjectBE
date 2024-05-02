package PizzaApp.api.repos;

import PizzaApp.api.entity.address.Address;
import PizzaApp.api.repos.address.AddressRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
public class AddressRepositoryTests {

	@Autowired
	private AddressRepository addressRepository;

	@Test
	public void givenAddress_thenCreateAddress() {
		// Arrange

		Address testAddress = new Address.Builder()
				.withStreet("Test Street")
				.withStreetNr(9)
				.build();

		// Act

		addressRepository.save(testAddress);
		long addressCount = addressRepository.count();

		// Assert
		assertThat(addressCount).isEqualTo(1);
	}
}