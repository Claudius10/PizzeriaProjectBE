package PizzaApp.api.repos.user.role;

import org.springframework.stereotype.Repository;

import PizzaApp.api.entity.user.Role;
import jakarta.persistence.EntityManager;

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
		return em.createQuery("select role from Role role where role.name = :roleName", Role.class)
				.setParameter("roleName", roleName)
				.getResultStream().findFirst();
	}
}
