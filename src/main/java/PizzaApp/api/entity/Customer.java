package PizzaApp.api.entity;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "tel")
	private int tel;

	@Column(name = "email")
	private String email;

	@OneToMany(mappedBy = "customer", cascade = jakarta.persistence.CascadeType.ALL)
	// The mappedBy attribute tells that the @ManyToOne side is in charge of
	// managing the Foreign Key column,
	// and the collection is used only to fetch the child entities and
	// to cascade parent entity state changes to children
	// MappedBy allows you to still link from the table not containing the FK
	// constraint to the other table.
	private List<Order> order;
	// map the child entities as a collection in the parent object
	// with the OneToMany annotation

	public Customer() {
	}

	public Customer(Long id, String firstName, String lastName, int tel, String email, List<Order> order) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.tel = tel;
		this.email = email;
		this.order = order;
	}

	// the parent entity Customer implements two utility methods which are used
	// to synchronize both sides of the bidirectional association
	public List<Order> getOrder() {
		return order;
	}

	public void setOrder(List<Order> order) {
		this.order = order;
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

	public int getTel() {
		return tel;
	}

	public void setTel(int tel) {
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
				+ ", email=" + email + ", order=" + order + "]";
	}
}
