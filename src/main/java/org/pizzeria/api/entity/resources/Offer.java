package org.pizzeria.api.entity.resources;

import jakarta.persistence.*;

@Entity
@Table(name = "offer")
public class Offer {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "offer_generator")
	@SequenceGenerator(name = "offer_generator", sequenceName = "offer_seq", allocationSize = 1)
	private Long id;

	@Column(name = "image")
	private String image;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "caveat")
	private String caveat;

	public Offer() {
		// The JPA specification requires all Entity classes to have a default no-arg constructor.
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCaveat() {
		return caveat;
	}

	public void setCaveat(String caveat) {
		this.caveat = caveat;
	}
}