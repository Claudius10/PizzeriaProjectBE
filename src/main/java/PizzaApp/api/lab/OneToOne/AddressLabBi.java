package PizzaApp.api.lab.OneToOne;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "address_lab")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AddressLabBi {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String addressName;

	@OneToOne(mappedBy = "addressLabBi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private OrderLabBi order;

	public AddressLabBi() {
	}

	public AddressLabBi(Long id, String addressName, OrderLabBi order) {
		this.id = id;
		this.addressName = addressName;
		this.order = order;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public OrderLabBi getOrder() {
		return order;
	}

	// for bi
	public void setOrder(OrderLabBi order) {
		this.order = order;
		order.setAddressLabBi(this);
	}
	
	/*
	 * for bi
	public void setOrder(List<OrderLabBi> order) {
		this.order = order;
		for (OrderLabBi item : order) {
			item.setAddressLabBi(this);
		}
	}
	*/
}
