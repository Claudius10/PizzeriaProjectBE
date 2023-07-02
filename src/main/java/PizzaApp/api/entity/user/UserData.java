package PizzaApp.api.entity.user;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import PizzaApp.api.entity.common.Address;
import PizzaApp.api.entity.common.Telephone;
import PizzaApp.api.entity.order.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

@Entity(name = "UserData")
@Table(name = "user_data")
public class UserData {

	@Id
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JsonIgnore
	private User user;

	@Column(name = "name")
	@Pattern(regexp = "^[a-zA-Z\s]{2,50}$",
			message = "Nombre / apellidos: solo letras sin tildes (mín 2, máx 25 letras)")
	private String name;

	@Column(name = "email")
	private String email;

	@OneToMany(mappedBy = "userData",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	@JsonManagedReference
	private List<Telephone> telephoneList;

	@ManyToMany(fetch = FetchType.LAZY,
			cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "users_addresses",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "address_id"))
	private Set<Address> addressList;

	@OneToMany(mappedBy = "userData",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	@JsonManagedReference
	private List<Order> orderList;

	public UserData() {
	}

	public UserData(String name, String email) {
		this.name = name;
		this.email = email;
		this.telephoneList = new ArrayList<>();
		this.orderList = new ArrayList<>();
		this.addressList = new HashSet<>();
	}

	public void addTel(Telephone tel) {
		telephoneList.add(tel);
		tel.setUserData(this);
	}

	public void removeTel(Telephone tel) {
		telephoneList.remove(tel);
		tel.setUserData(null);
	}

	public void addOrder(Order order) {
		orderList.add(order);
		order.setUserData(this);
	}

	public void removeOrder(Order order) {
		orderList.remove(order);
		order.setUserData(null);
	}

	public void addAddress(Address address) {
		this.addressList.add(address);
	}

	public void removeAddress(Address address) {
		this.addressList.remove(address);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Telephone> getTelephoneList() {
		return telephoneList;
	}

	public void setTelephoneList(List<Telephone> telephoneList) {
		this.telephoneList = telephoneList;
	}

	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}

	public Set<Address> getAddressList() {
		return addressList;
	}

	public void setAddressList(Set<Address> addressList) {
		this.addressList = addressList;
	}
}