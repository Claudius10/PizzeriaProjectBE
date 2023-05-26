package PizzaApp.api.entity.clients.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.annotations.NaturalId;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import PizzaApp.api.entity.clients.Address;
import PizzaApp.api.entity.clients.Telephone;
import PizzaApp.api.entity.order.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NaturalId
	@Column(name = "username", unique = true)
	private String username;

	@Column(name = "password")
	private String password;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonManagedReference
	@Valid
	private Telephone tel;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Order> orders = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
			CascadeType.DETACH })
	@JoinTable(name = "users_addresses", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "address_id"))
	private List<Address> addressList = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
			CascadeType.DETACH })
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public User() {
	}

	public User(Long id, String username, String password, Telephone tel, List<Order> orders, List<Address> addressList,
			Set<Role> roles) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.tel = tel;
		this.orders = orders;
		this.addressList = addressList;
		this.roles = roles;
	}

	public User(Long id, String username, String password, Telephone tel, List<Order> orders,
			List<Address> addressList) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.tel = tel;
		this.orders = orders;
		this.addressList = addressList;
	}

	public User(String username, String password, Telephone tel, List<Order> orders, List<Address> addressList) {
		this.username = username;
		this.password = password;
		this.tel = tel;
		this.orders = orders;
		this.addressList = addressList;
	}

	public User(String username, String password, Telephone tel) {
		this.username = username;
		this.password = password;
		this.tel = tel;
	}

	public void setTel(Telephone tel) {
		if (tel == null) {
			if (this.tel != null) {
				this.tel.setUser(null);
			}
		} else {
			tel.setUser(this);
		}
		this.tel = tel;
	}

	public void addOrder(Order order) {
		orders.add(order);
		order.setUser(this);

	}

	public void removeOrder(Order order) {
		orders.remove(order);
		order.setUser(null);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Telephone getTel() {
		return tel;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<Address> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<Address> addressList) {
		this.addressList = addressList;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}