package PizzaApp.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "store")
public class Store {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "location")
	private String location;

	@Column(name = "phone_number")
	private int phoneNumber;

	@Column(name = "schedule")
	private String schedule;

	/*
	 * // storePickUp in Order class (entity)
	 * 
	 * @OneToMany(mappedBy = "storePickUp", cascade =
	 * jakarta.persistence.CascadeType.ALL) private List<Order> ordersHandled;
	 */
	
	public Store() {
	};

	public Store(Long id, String name, String location, int phoneNumber, String schedule) {
		this.id = id;
		this.name = name;
		this.location = location;
		this.phoneNumber = phoneNumber;
		this.schedule = schedule;
		// this.ordersHandled = ordersHandled;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	/*
	 * public List<Order> getOrdersHandled() { return ordersHandled; }
	 * 
	 * public void setOrdersHandled(List<Order> ordersHandled) { this.ordersHandled
	 * = ordersHandled; }
	 */
}
