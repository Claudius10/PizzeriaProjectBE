package PizzaApp.api.entity.address;

import PizzaApp.api.exceptions.constraints.ValidAddress;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;

import java.util.Objects;

@Entity(name = "Address")
@Table(name = "address")
@Valid
@ValidAddress
public class Address {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "street")
	private String street;

	@Column(name = "street_nr")
	private Integer streetNr;

	@Column(name = "gate")
	private String gate;

	@Column(name = "staircase")
	private String staircase;

	@Column(name = "floor")
	private String floor;

	@Column(name = "door")
	private String door;

	public Address() {
	}

	private Address(Builder builder) {
		this.id = builder.id;
		this.street = builder.street;
		this.streetNr = builder.streetNr;
		this.gate = builder.gate;
		this.staircase = builder.staircase;
		this.floor = builder.floor;
		this.door = builder.door;
	}

	public static class Builder {
		private Long id;
		private String street;
		private Integer streetNr;
		private String gate;
		private String staircase;
		private String floor;
		private String door;

		public Builder() {
		}

		public Builder withId(Long id) {
			this.id = id;
			return this;
		}

		public Builder withStreet(String street) {
			this.street = street;
			return this;
		}

		public Builder withStreetNr(Integer number) {
			this.streetNr = number;
			return this;
		}

		public Builder withGate(String gate) {
			this.gate = gate;
			return this;
		}

		public Builder withStaircase(String staircase) {
			this.staircase = staircase;
			return this;
		}

		public Builder withFloor(String floor) {
			this.floor = floor;
			return this;
		}

		public Builder withDoor(String door) {
			this.door = door;
			return this;
		}

		public Address build() {
			return new Address(this);
		}
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getStreetNr() {
		return streetNr;
	}

	public void setStreetNr(Integer streetNr) {
		this.streetNr = streetNr;
	}

	public String getGate() {
		return gate;
	}

	public void setGate(String gate) {
		this.gate = gate;
	}

	public String getStaircase() {
		return staircase;
	}

	public void setStaircase(String staircase) {
		this.staircase = staircase;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getDoor() {
		return door;
	}

	public void setDoor(String door) {
		this.door = door;
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", street=" + street + ", streetNr=" + streetNr + ", gate=" + gate + ", staircase="
				+ staircase + ", floor=" + floor + ", door=" + door + "]";
	}

	public boolean entityEquals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Address address = (Address) o;
		return Objects.equals(street, address.street)
				&& Objects.equals(streetNr, address.streetNr)
				&& Objects.equals(gate, address.gate)
				&& Objects.equals(staircase, address.staircase)
				&& Objects.equals(floor, address.floor)
				&& Objects.equals(door, address.door);
	}
}
