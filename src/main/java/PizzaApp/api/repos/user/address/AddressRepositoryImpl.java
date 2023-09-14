package PizzaApp.api.repos.user.address;

import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.user.Address;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

@Repository
public class AddressRepositoryImpl implements AddressRepository {

	private final EntityManager em;

	public AddressRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public Address findReference(String id) {
		return em.getReference(Address.class, id);
	}

	@Override
	public Optional<Address> findAddress(Address address) {
		return em.createQuery(
						"select address from Address address where address.street = :street " +
								"and address.streetNr = :streetNumber " +
								"and (:gate is null or address.gate = :gate) " +
								"and (:staircase is null or address.staircase = :staircase) " +
								"and (:floor is null or address.floor = :floor) " +
								"and (:door is null or address.door = :door)",
						Address.class)
				.setParameter("street", address.getStreet())
				.setParameter("streetNumber", address.getStreetNr())
				.setParameter("gate", address.getGate())
				.setParameter("staircase", address.getStaircase())
				.setParameter("floor", address.getFloor())
				.setParameter("door", address.getDoor())
				.getResultStream().findFirst();
	}

	@Override
	public List<Address> findAllByUserId(String id) {
		return em.createQuery(
						"select userData.addressList from UserData userData where userData.id = :userId",
						Address.class)
				.setParameter("userId", id)
				.getResultList();
	}

	@Override
	public Long findUserAddressListSize(String id) {
		return (Long) em.createNativeQuery("select count(*) from users_addresses where user_id=:userId", Long.class)
				.setParameter("userId", id)
				.getSingleResult();
	}
}