package PizzaApp.api.repos.common.resources;

import java.util.List;

import org.springframework.stereotype.Repository;

import PizzaApp.api.entity.resources.Offer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class OfferRepositoryImpl implements OfferRepository {

	private final EntityManager em;

	public OfferRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<Offer> findAll() {
		TypedQuery<Offer> query = em.createQuery("from Offer", Offer.class);
		return query.getResultList();
	}
}
