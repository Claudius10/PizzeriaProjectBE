package PizzaApp.api.repos.role;
import org.springframework.stereotype.Repository;

import PizzaApp.api.entity.clients.user.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

	private EntityManager em;

	public RoleRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public Role findByName(String roleName) {
		try {

			TypedQuery<Role> query = em.createQuery("from Role r where r.name=:roleName", Role.class);
			query.setParameter("roleName", roleName);
			return query.getSingleResult();
			
		} catch (NoResultException nre) {
			return null;
		}
	}
}
