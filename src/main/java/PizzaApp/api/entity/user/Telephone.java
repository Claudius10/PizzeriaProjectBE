package PizzaApp.api.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import PizzaApp.api.exceptions.constraints.IntegerLength;
import jakarta.persistence.*;

@Entity(name = "Telephone")
@Table(name = "telephone")
public class Telephone {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "number")
	@IntegerLength(min = 9, max = 9, message = "Teléfono: mín 9 digitos, máx 9 digitos")
	private Integer number;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private UserData userData;

	public Telephone() {
	}

	public Telephone(Integer telephone) {
		this.number = telephone;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (!(obj instanceof Telephone))
			return false;

		return id != null && id.equals(((Telephone) obj).getId());
	}
}