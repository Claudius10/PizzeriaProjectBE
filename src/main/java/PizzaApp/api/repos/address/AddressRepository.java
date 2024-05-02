package PizzaApp.api.repos.address;

import PizzaApp.api.entity.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

	@Query("from Address address where address.id = :addressId")
	Address findUserAddressById(Long addressId);
}