package PizzaApp.api.lab.OneToOne;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "address_lab")
public class AddressLab {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name")
	private String addressName;

	public AddressLab() {
	}

	public AddressLab(Long id, String addressName) {
		this.id = id;
		this.addressName = addressName;
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

	@Override
	public String toString() {
		return "AddressLab [id=" + id + ", addressName=" + addressName + "]";
	}
}
