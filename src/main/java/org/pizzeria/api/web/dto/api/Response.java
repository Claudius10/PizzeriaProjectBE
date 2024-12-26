package org.pizzeria.api.web.dto.api;

import lombok.*;
import org.pizzeria.api.entity.error.Error;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Response {

	@Builder.Default
	private final String timeStamp = LocalDateTime.now().toString();

	private Status status;

	private Object payload;

	private Error error;
}