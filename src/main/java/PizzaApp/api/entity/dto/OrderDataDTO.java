package PizzaApp.api.entity.dto;
import PizzaApp.api.entity.clients.Address;
import PizzaApp.api.entity.clients.Email;

public class OrderDataDTO {

	private Address address;
	private Email email;

	public OrderDataDTO() {
	}

	public OrderDataDTO(Address address, Email email) {
		this.address = address;
		this.email = email;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}
}
