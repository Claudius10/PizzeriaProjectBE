package PizzaApp.api.lab.OneToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

//order_lab FK references address_lab PK
//Order is child of Address
@Entity
@Table(name = "order_lab")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class OrderLabBi {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String orderName;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private AddressLabBi addressLabBi;

	public OrderLabBi() {
	}

	public OrderLabBi(Long id, String orderName, AddressLabBi addressLabBi) {
		this.id = id;
		this.orderName = orderName;
		this.addressLabBi = addressLabBi;
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

	public AddressLabBi getAddressLabBi() {
		return addressLabBi;
	}

	public void setAddressLabBi(AddressLabBi addressLabBi) {
		this.addressLabBi = addressLabBi;
	}

	@Override
	public String toString() {
		return "OrderLabBi [id=" + id + ", orderName=" + orderName + ", addressLabBi=" + addressLabBi + "]";
	}
}
