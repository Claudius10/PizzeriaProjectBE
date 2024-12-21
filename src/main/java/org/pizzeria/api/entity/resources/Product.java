package org.pizzeria.api.entity.resources;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Map;

@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
	@SequenceGenerator(name = "product_generator", sequenceName = "product_seq", allocationSize = 1)
	private Long id;

	private String productType;

	private String image;

	@Type(JsonType.class)
	@Column(columnDefinition = "json")
	private Map<String, String> name;

	@Type(JsonType.class)
	@Column(columnDefinition = "json")
	private Map<String, String> description;

	@Type(JsonType.class)
	@Column(columnDefinition = "json")
	private Map<String, Double> price;

	@Type(JsonType.class)
	@Column(columnDefinition = "json")
	private Map<String, Map<String, String>> format; // <"m", <"en": "Medium">, <"es": "Mediana">; "l", <"en": "Familiar">,<"es": "Familiar">>
}