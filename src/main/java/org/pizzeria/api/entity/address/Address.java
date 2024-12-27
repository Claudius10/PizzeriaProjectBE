package org.pizzeria.api.entity.address;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.pizzeria.api.web.exceptions.constraints.annotation.ValidAddress;

import java.util.Objects;

@Entity(name = "Address")
@Table(name = "address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
@Valid
@ValidAddress
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_generator")
	@SequenceGenerator(name = "address_generator", sequenceName = "address_seq", allocationSize = 1)
	private Long id;

	@Column(nullable = false)
	private String street;

	@Column(nullable = false)
	private Integer number;

	private String details;

	public boolean contentEquals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Address address = (Address) o;
		return Objects.equals(street, address.street)
				&& Objects.equals(number, address.number)
				&& Objects.equals(details, address.details);
	}
}