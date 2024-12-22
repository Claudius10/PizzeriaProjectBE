package org.pizzeria.api.web.dto.api;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Status {

	private int code;

	private String description;

	private boolean isError;
}
