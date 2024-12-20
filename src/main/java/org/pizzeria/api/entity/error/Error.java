package org.pizzeria.api.entity.error;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "error")
public class Error {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "error_generator")
	@SequenceGenerator(name = "error_generator", sequenceName = "error_seq", allocationSize = 1)
	private Long id;

	private String cause;

	private String message;

	private String origin;

	private String path;

	private boolean logged;

	private boolean fatal;
}