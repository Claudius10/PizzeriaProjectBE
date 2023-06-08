package PizzaApp.api.repos.address;

import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.clients.Address;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class AddressRepositoryImpl implements AddressRepository {

	private final EntityManager em;

	public AddressRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public Address findAddress(Address address) {

		try {
			TypedQuery<Address> query = em.createQuery(
					"from Address a where a.street=:oStreet and a.streetNr=:oNumber and (:oGate is null or a.gate = :oGate) "
							+ "and (:oStaircase is null or a.staircase= :oStaircase) and (:oFloor is null or a.floor= :oFloor) and (:oDoor is null or a.door= :oDoor)",
					Address.class);

			query.setParameter("oStreet", address.getStreet());
			query.setParameter("oNumber", address.getStreetNr());
			query.setParameter("oGate", address.getGate());
			query.setParameter("oStaircase", address.getStaircase());
			query.setParameter("oFloor", address.getFloor());
			query.setParameter("oDoor", address.getDoor());

			return query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}
}