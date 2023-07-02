package PizzaApp.api.repos.common.resources;

import java.util.List;

import PizzaApp.api.entity.resources.Store;

public interface StoreRepository {

	List<Store> findAll();
}
