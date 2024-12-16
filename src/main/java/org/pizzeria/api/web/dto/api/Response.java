package org.pizzeria.api.web.dto.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class Response {

	@Builder.Default
	private final String timeStamp = LocalDateTime.now().toString();

	private Status status;

	private ApiError error;

	private Object payload;
}