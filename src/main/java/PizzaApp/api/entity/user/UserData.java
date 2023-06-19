package PizzaApp.api.entity.user;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import PizzaApp.api.entity.user.common.Address;
import PizzaApp.api.entity.user.common.Telephone;
import PizzaApp.api.entity.order.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

@Entity(name = "UserData")
@Table(name = "user_data")
public class UserData {

	@Id
	private Long id;

	@OneToOne
	@MapsId
	private User user;

	@Column(name = "name")
	@Pattern(regexp = "^[a-zA-Z\s]{2,50}$",
			message = "Nombre / apellidos: solo letras sin tildes (mín 2, máx 25 letras)")
	private String firstAndLastName;

	@OneToMany(mappedBy = "userData",
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.EAGER)
	@JsonManagedReference
	private List<Telephone> telephoneList;

	@OneToMany(mappedBy = "userData",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	@JsonManagedReference
	private List<Order> orderList;

	@ManyToMany(fetch = FetchType.LAZY,
			cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "users_addresses",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "address_id"))
	private Set<Address> addressList;

	public UserData() {
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
}