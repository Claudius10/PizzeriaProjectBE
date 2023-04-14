package PizzaApp.api.entity.clients.customer;

import PizzaApp.api.exceptions.constraints.IntegerLength;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_tel")
public class Telephone {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "number")
	@IntegerLength(min = 9, max = 9, message = "Teléfono: mín 9 digitos, máx 9 digitos")
	private int number;

	public Telephone() {
	}

	public Telephone(Long id, int number) {
		this.id = id;
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
}
