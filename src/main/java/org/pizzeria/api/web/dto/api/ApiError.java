package org.pizzeria.api.web.dto.api;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiError {

	private String cause;

	private String message;

	private String origin;

	private boolean logged;
}