package org.pizzeria.api.entity.resources;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pizzeria.api.entity.address.Address;

@Entity(name = "Store")
@Table(name = "store")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
}