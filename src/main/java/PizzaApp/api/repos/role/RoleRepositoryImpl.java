package PizzaApp.api.repos.role;

import org.springframework.stereotype.Repository;

import PizzaApp.api.entity.user.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

	private final EntityManager em;

	public RoleRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public void create(Role role) {
		em.persist(role);
	}

	@Override
	public Optional<Role> findByName(String roleName) {
		TypedQuery<Role> query = em.createQuery("select r from Role r where r.name = :roleName", Role.class);
		query.setParameter("roleName", roleName);
		return query.getResultStream().findFirst();

		// alternative
		/*
		try {
			return Optional.of(query.getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}*/
	}
}
