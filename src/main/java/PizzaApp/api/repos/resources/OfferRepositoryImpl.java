package PizzaApp.api.repos.resources;

import java.util.List;
import org.springframework.stereotype.Repository;

import PizzaApp.api.entity.resources.Offer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class OfferRepositoryImpl implements OfferRepository {

	private EntityManager em;

	public OfferRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<Offer> findAll() {
		TypedQuery<Offer> query = em.createQuery("from Offer", Offer.class);
		List<Offer> offers = query.getResultList();
		return offers;
	}
}