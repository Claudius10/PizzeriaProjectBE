package PizzaApp.api.entity.user;

import PizzaApp.api.exceptions.constraints.IntegerLength;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

@Entity(name = "Address")
@Table(name = "address")
@Valid
public class Address {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "street")
	@Pattern(regexp = "^[a-zA-Z0-9.,:;()\s]{2,25}$", message = "Calle: solo letras sin tildes y números (mín 2, máx 25 letras)")
	private String street;

	@Column(name = "street_nr")
	@IntegerLength(min = 1, max = 4, message = "Número de calle: mín 1 digitos, máx 4 digitos")
	private Integer streetNr;

	@Column(name = "gate")
	@Pattern(regexp = "^[a-zA-Z0-9.,:;\s]{0,25}$", message = "Portal: solo letras sin tildes y números (mín 0, máx 25 letras)")
	private String gate;

	@Column(name = "staircase")
	@Pattern(regexp = "^[a-zA-Z0-9.,:;\s]{0,25}$", message = "Escalera: solo letras sin tildes y números (mín 0, máx 25 letras)")
	private String staircase;

	@Column(name = "floor")
	@Pattern(regexp = "^[a-zA-Z0-9.,:;\s]{0,25}$", message = "Piso: solo letras sin tildes y números (mín 0, máx 25 letras)")
	private String floor;

	@Column(name = "door")
	@Pattern(regexp = "^[a-zA-Z0-9.,:;\s]{0,25}$", message = "Puerta: solo letras sin tildes y números (mín 0, máx 25 letras)")
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
