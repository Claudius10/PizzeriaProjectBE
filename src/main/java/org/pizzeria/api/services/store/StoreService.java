package org.pizzeria.api.services.store;

import org.pizzeria.api.entity.resources.Store;

import java.util.List;
import java.util.Map;

public interface StoreService {

	void createStore(Long addressId, String name, Integer number, Map<String, String> schedule, String image);

	List<Store> findAll();
}