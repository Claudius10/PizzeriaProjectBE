package PizzaApp.api.services.store;

import PizzaApp.api.entity.resources.Store;

import java.util.List;

public interface StoreService {

	void createStore(Long addressId, String name, Integer number, String schedule);

	List<Store> findAll();
}