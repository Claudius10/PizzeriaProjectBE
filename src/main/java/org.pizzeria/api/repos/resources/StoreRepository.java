package org.pizzeria.api.repos.resources;

import org.pizzeria.api.entity.resources.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

}