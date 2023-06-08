package PizzaApp.api.entity.clients;

import com.fasterxml.jackson.annotation.JsonBackReference;
import PizzaApp.api.entity.clients.user.User;
import PizzaApp.api.validation.constraints.IntegerLength;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;

@Entity
@Table(name = "telephone")
public class Telephone {

	@Id
	private Long id;

	@Column(name = "number")
	@IntegerLength(min = 9, max = 9, message = "Teléfono: mín 9 digitos, máx 9 digitos")
	private int number;

	@OneToOne
	@MapsId
	@JsonBackReference
	@Valid
	private User user;

	public Telephone() {
	}

	public Telephone(Long id, int number, User user) {
		this.id = id;
		this.number = number;
		this.user = user;
	}

	public Telephone(int number, User user) {
		this.number = number;
		this.user = user;
	}

	public Telephone(int number) {
		this.number = number;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}