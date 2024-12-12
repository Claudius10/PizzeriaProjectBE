package org.pizzeria.api.entity.resources;

import jakarta.persistence.*;
import org.pizzeria.api.entity.address.Address;

@Entity(name = "Store")
@Table(name = "store")
public class Store {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "store_generator")
	@SequenceGenerator(name = "store_generator", sequenceName = "store_seq", allocationSize = 1)
	private Long id;

	@Column
	private String name;

	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	private Address address;

	@Column
	private Integer phoneNumber;

	@Column
	private String schedule;

	@Column
	private String image;

	public Store(Long id, String name, Address address, Integer phoneNumber, String schedule, String image) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.schedule = schedule;
		this.image = image;
	}

	public Store() {
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Address getAddress() {
		return address;
	}

	public Integer getPhoneNumber() {
		return phoneNumber;
	}

	public String getSchedule() {
		return schedule;
	}

	public String getImage() {
		return image;
	}
}