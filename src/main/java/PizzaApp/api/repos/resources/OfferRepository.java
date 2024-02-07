package PizzaApp.api.repos.resources;

import java.util.List;

import PizzaApp.api.entity.resources.Offer;

public interface OfferRepository {

	List<Offer> findAll();
}
