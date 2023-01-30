package PizzaApp.api.lab.OneToOne;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

// order_lab FK references address_lab PK
// Order is child of Address
@Entity
@Table(name = "order_lab")
public class OrderLab {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name")
	private String orderName;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private AddressLab addressLab;

	public OrderLab() {
	}

	public OrderLab(Long id, String orderName, AddressLab addressLab) {
		this.id = id;
		this.orderName = orderName;
		this.addressLab = addressLab;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public AddressLab getAddressLab() {
		return addressLab;
	}

	public void setAddressLab(AddressLab addressLab) {
		this.addressLab = addressLab;
	}

	@Override
	public String toString() {
		return "OrderLab [id=" + id + ", orderName=" + orderName + ", addressLab=" + addressLab + "]";
	}
}

