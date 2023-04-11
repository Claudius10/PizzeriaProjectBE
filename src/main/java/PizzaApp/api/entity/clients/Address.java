package PizzaApp.api.entity.clients;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;


@Entity
@Table(name = "address")
public class Address {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "street")
	@NotEmpty(message = "El nombre de la calle no puede faltar.")
	private String street;

	@Column(name = "street_nr")
	private int streetNr;

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

	public Address(Long id, String street, int streetNr, String gate, String staircase, String floor, String door) {
		this.id = id;
		this.street = street;
		this.streetNr = streetNr;
		this.gate = gate;
		this.staircase = staircase;
		this.floor = floor;
		this.door = door;
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

	public int getStreetNr() {
		return streetNr;
	}

	public void setStreetNr(int streetNr) {
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
}
