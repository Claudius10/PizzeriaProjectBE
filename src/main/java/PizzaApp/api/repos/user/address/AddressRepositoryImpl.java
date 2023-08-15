package PizzaApp.api.repos.user.address;

import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.user.Address;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

@Repository
public class AddressRepositoryImpl implements AddressRepository {

	private final EntityManager em;

	public AddressRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public Address findReference(Long id) {
		return em.getReference(Address.class, id);
	}

	@Override
	public Optional<Address> findAddress(Address address) {
		TypedQuery<Address> query = em.createQuery(
				"select a from Address a where a.street = :oStreet and a.streetNr = :oNumber and (:oGate is null or a.gate = " +
						":oGate) and (:oStaircase is null or a.staircase = :oStaircase) and (:oFloor is null or a.floor = :oFloor) " +
						"and (:oDoor is null or a.door = :oDoor)",
				Address.class);
		query.setParameter("oStreet", address.getStreet());
		query.setParameter("oNumber", address.getStreetNr());
		query.setParameter("oGate", address.getGate());
		query.setParameter("oStaircase", address.getStaircase());
		query.setParameter("oFloor", address.getFloor());
		query.setParameter("oDoor", address.getDoor());
		return query.getResultStream().findFirst();
	}

	@Override
	public Optional<List<Address>> findByUserId(Long id) {
		TypedQuery<Address> query = em.createQuery(
				"select userData.addressList from UserData userData where userData.id=:userId",
				Address.class);
		query.setParameter("userId", id);
		Optional<List<Address>> resultList = Optional.ofNullable(query.getResultList());
		return resultList.isEmpty() ? Optional.empty() : resultList;
	}

	@Override
	public Long findUserAddressListSize(Long id) {
		Query query = em.createNativeQuery("select count(*) from users_addresses where user_id=:userId", Long.class);
		query.setParameter("userId", id);
		return (Long) query.getSingleResult();
	}
}