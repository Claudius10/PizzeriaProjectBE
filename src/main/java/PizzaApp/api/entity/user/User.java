package PizzaApp.api.entity.user;

import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.role.Role;
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

	private String name;

	@Column(unique = true)
	private String email;

	@Column(unique = true)
	private Integer contactNumber;

	private String password;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinTable(name = "users_addresses",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "address_id"))
	private Set<Address> addressList;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinTable(name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

	public User() {
	}

	public void addAddress(Address address) {
		this.addressList.add(address);
	}

	public void removeAddress(Address address) {
		this.addressList.remove(address);
	}

	private User(Builder builder) {
		this.name = builder.name;
		this.email = builder.email;
		this.contactNumber = builder.contactNumber;
		this.password = builder.password;
		this.roles = builder.roles;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public Integer getContactNumber() {
		return contactNumber;
	}

	public Set<Address> getAddressList() {
		return addressList;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setAddressList(Set<Address> addressList) {
		this.addressList = addressList;
	}

	@Override
	public String getUsername() {
		return this.email;
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
			return this.email.equals(((User) obj).email);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.email.hashCode();
	}

	public static class Builder {

		private String email, password, name;

		private Integer contactNumber;

		private final Set<Role> roles;

		public Builder() {
			this.roles = new HashSet<>();
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withEmail(String email) {
			this.email = email;
			return this;
		}

		public Builder withContactNumber(Integer contactNumber) {
			this.contactNumber = contactNumber;
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
}
