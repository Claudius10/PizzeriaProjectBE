package PizzaApp.api.repos.common.resources;

import java.util.List;

import org.springframework.stereotype.Repository;

import PizzaApp.api.entity.resources.Offer;
import jakarta.persistence.EntityManager;

@Repository
public class OfferRepositoryImpl implements OfferRepository {

	private final EntityManager em;

	public OfferRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<Offer> findAll() {
		return em.createQuery("select offer from Offer offer", Offer.class).getResultList();
	}
}
