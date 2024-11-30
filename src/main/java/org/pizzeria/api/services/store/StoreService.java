package org.pizzeria.api.services.store;

import org.pizzeria.api.entity.resources.Store;

import java.util.List;

public interface StoreService {

	void createStore(Long addressId, String name, Integer number, String schedule, String image);

	List<Store> findAll();
}