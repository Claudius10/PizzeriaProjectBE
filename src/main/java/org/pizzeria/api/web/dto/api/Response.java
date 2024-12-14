package org.pizzeria.api.web.dto.api;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class Response {

	@Builder.Default
	private final String timeStamp = LocalDateTime.now().toString();

	private Status status;

	private ApiError error;

	private Object payload;
}