package PizzaApp.api.utility.order;

import PizzaApp.api.entity.common.Address;
import PizzaApp.api.entity.common.Email;

public class OrderData {

	private Address address;
	private Email email;

	public OrderData() {
	}

	public OrderData(Address address, Email email) {
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
