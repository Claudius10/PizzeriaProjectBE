package PizzaApp.api.repos.address;

import java.util.List;

import org.springframework.stereotype.Repository;
import PizzaApp.api.entity.clients.Address;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class AddressRepository {

	private EntityManager em;

	public AddressRepository(EntityManager em) {
		this.em = em;
	}

	public Address findAddress(Address address) {

		try {
			TypedQuery<Address> query = em.createQuery(
					"from Address a where a.street=:oStreet " + "and a.streetNr=:oNumber " + "and a.gate=:oGate "
							+ "and a.staircase=:oStaircase " + "and a.floor=:oFloor " + "and a.door=:oDoor",
					Address.class);

			query.setParameter("oStreet", address.getStreet());
			query.setParameter("oNumber", address.getStreetNr());
			query.setParameter("oGate", address.getGate());
			query.setParameter("oStaircase", address.getStaircase());
			query.setParameter("oFloor", address.getFloor());
			query.setParameter("oDoor", address.getDoor());

			return query.getSingleResult();
		} catch (NoResultException | NullPointerException e) {
			return null;
		}
	}

	// this method is used testing purposes
	public List<Address> findAddresses(Address address) {

		TypedQuery<Address> query = em
				.createQuery(
						"from Address a where a.street=:oStreet " + "and a.streetNr=:oNumber " + "and a.gate=:oGate "
								+ "and a.staircase=:oStaircase " + "and a.floor=:oFloor " + "and a.door=:oDoor",
						Address.class);

		query.setParameter("oStreet", address.getStreet());
		query.setParameter("oNumber", address.getStreetNr());
		query.setParameter("oGate", address.getGate());
		query.setParameter("oStaircase", address.getStaircase());
		query.setParameter("oFloor", address.getFloor());
		query.setParameter("oDoor", address.getDoor());

		if (query.getResultList().isEmpty()) {
			throw new NoResultException("No address found matching given address.");
		} else {
			return query.getResultList();
		}
	}
}