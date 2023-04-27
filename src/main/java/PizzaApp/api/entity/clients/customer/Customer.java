package PizzaApp.api.entity.clients.customer;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "first_name")
	@Pattern(regexp = "^[a-zA-Z\s]{2,25}$", message = "Nombre: solo letras sin tildes (mín 2, máx 25 letras)")
	private String firstName;

	@Column(name = "last_name")
	@Pattern(regexp = "^[a-zA-Z\s]{0,25}$", message = "Appelido: solo letras sin tildes (no mín, máx 25 letras)")
	private String lastName;

	@Column(name = "email")
	@Email(message = "Email: formato del email no aceptado")
	@NotBlank(message = "Email: el valor no puede ser vacío")
	private String email;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH })
	@JoinColumn(name = "tel_id")
	@Valid
	private Telephone tel;

	public Customer() {
	}

	public Customer(Long id, String firstName, String lastName, String email, Telephone tel) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.tel = tel;
	}

	// constructor with no id
	public Customer(String firstName, String lastName, String email, Telephone tel) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.tel = tel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Telephone getTel() {
		return tel;
	}

	public void setTel(Telephone tel) {
		this.tel = tel;
	}
}
