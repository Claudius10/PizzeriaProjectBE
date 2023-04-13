package PizzaApp.api.entity.clients;
import PizzaApp.api.exceptions.constraints.IntegerLength;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

	@Column(name = "tel")
	@IntegerLength(min = 9, max = 9, message = "Teléfono: mín 9 digitos, máx 9 digitos")
	private Integer tel;

	@Column(name = "email")
	@Email(message = "Email: formato del email no aceptado")
	@NotBlank(message = "Email: el valor no puede ser vacío")
	private String email;

	public Customer() {
	}

	public Customer(Long id, String firstName, String lastName, Integer tel, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.tel = tel;
		this.email = email;
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

	public Integer getTel() {
		return tel;
	}

	public void setTel(Integer tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", tel=" + tel
				+ ", email=" + email + "]";
	}
}
