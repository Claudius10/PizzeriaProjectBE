package PizzaApp.api.entity.clients;
import PizzaApp.api.validation.constraints.IntegerLength;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "address")
public class Address {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "street")
	@Pattern(regexp = "^[a-zA-Z0-9.,:;\s]{2,25}$", message = "Calle: solo letras sin tildes y números (mín 2, máx 25 letras)")
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

	public Address(Long id, String street, Integer streetNr, String gate, String staircase, String floor, String door) {
		this.id = id;
		this.street = street;
		this.streetNr = streetNr;
		this.gate = gate;
		this.staircase = staircase;
		this.floor = floor;
		this.door = door;
	}
	
    // constructor without id
	public Address(String street, Integer streetNr, String gate, String staircase, String floor, String door) {
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
}
