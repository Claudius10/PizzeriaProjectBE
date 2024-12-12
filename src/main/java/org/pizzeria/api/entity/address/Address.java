package org.pizzeria.api.entity.address;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.pizzeria.api.exceptions.constraints.annotation.ValidAddress;

import java.util.Objects;

@Entity(name = "Address")
@Table(name = "address")
@Valid
@ValidAddress
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_generator")
	@SequenceGenerator(name = "address_generator", sequenceName = "address_seq", allocationSize = 1)
	private Long id;

	@Column(nullable = false)
	private String street;

	@Column(nullable = false)
	private Integer number;

	@Column
	private String details;

	public Address() {
	}

	private Address(Builder builder) {
		this.street = builder.street;
		this.number = builder.number;
		this.details = builder.details;
	}

	public Long getId() {
		return id;
	}

	public String getStreet() {
		return street;
	}

	public Integer getNumber() {
		return number;
	}

	public String getDetails() {
		return details;
	}

	public boolean contentEquals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Address address = (Address) o;
		return Objects.equals(street, address.street)
				&& Objects.equals(number, address.number)
				&& Objects.equals(details, address.details);
	}

	public static class Builder {
		private String street;
		private Integer number;
		private String details;

		public Builder() {
		}

		public Builder withStreet(String street) {
			this.street = street;
			return this;
		}

		public Builder withNumber(Integer number) {
			this.number = number;
			return this;
		}

		public Builder withDetails(String details) {
			this.details = details;
			return this;
		}

		public Address build() {
			return new Address(this);
		}
	}
}