package org.pizzeria.api.web.dto.api;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Status {

	private int code;

	private String description;
}
