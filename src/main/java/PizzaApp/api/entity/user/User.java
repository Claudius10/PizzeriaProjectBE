package PizzaApp.api.entity.user;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity(name = "User")
@Table(name = "user")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String username;

	private String password;

	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

	public User() {
	}

	private User(Builder builder) {
		this.username = builder.username;
		this.password = builder.password;
		this.roles = builder.roles;
	}

	public static class Builder {

		private String username;
		private String password;
		private final Set<Role> roles = new HashSet<>();

		public Builder() {
		}

		public Builder withUsername(String username) {
			this.username = username;
			return this;
		}

		public Builder withPassword(String password) {
			this.password = password;
			return this;
		}

		public Builder withRoles(Role... roles) {
			this.roles.addAll(Arrays.asList(roles));
			return this;
		}

		public User build() {
			return new User(this);
		}
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			return this.username.equals(((User) obj).username);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.username.hashCode();
	}
}
