package PizzaApp.api.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

@Entity(name = "Role")
@Table(name = "role")
public class Role implements GrantedAuthority {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", unique = true)
	private String name;

	public Role() {
	}

	public Role(String name) {
		this.name = name;
	}

	@Override
	public String getAuthority() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof Role) {
			return this.name.equals(((Role) obj).name);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
}
