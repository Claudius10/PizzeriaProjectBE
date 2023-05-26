package PizzaApp.api.entity.clients;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "email")
public class Email {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "email")
	@jakarta.validation.constraints.Email(message = "Email: formato del email no aceptado")
	@NotBlank(message = "Email: el valor no puede ser vacío")
	private String email;

	public Email() {
	}

	public Email(Long id, String email) {
		this.id = id;
		this.email = email;
	}

	public Email(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Email [id=" + id + ", email=" + email + "]";
	}
}