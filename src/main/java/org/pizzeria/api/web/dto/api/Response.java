package org.pizzeria.api.web.dto.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.pizzeria.api.entity.error.Error;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class Response {

	@Builder.Default
	private final String timeStamp = LocalDateTime.now().toString();

	private Status status;

	private Object payload;

	private Error error;
}