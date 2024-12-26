package org.pizzeria.api.web.dto.api;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Status {

	private int code;

	private String description;

	private boolean isError;
}
