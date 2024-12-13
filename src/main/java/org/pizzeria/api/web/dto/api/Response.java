package org.pizzeria.api.web.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Response {

	@Builder.Default
	private final String timesStamp = LocalDateTime.now().toString();

	private int statusCode;

	private String statusDescription;

	private String errorClass;

	private String errorMessage;

	private Object data;
}