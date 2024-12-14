package org.pizzeria.api.web.dto.api;

import java.time.LocalDateTime;

public class Response {

	private final String timeStamp = LocalDateTime.now().toString();

	private int statusCode;

	private String statusDescription;

	private String errorClass;

	private String errorMessage;

	private String errorOrigin;

	private Object payload;

	public static Builder builder() {
		return new Builder();
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public String getErrorClass() {
		return errorClass;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getErrorOrigin() {
		return errorOrigin;
	}

	public Object getPayload() {
		return payload;
	}

	public static class Builder {

		private final Response response;

		public Builder() {
			response = new Response();
		}

		public Builder statusDescription(String statusDescription) {
			response.statusDescription = statusDescription;
			return this;
		}

		public Builder statusCode(int statusCode) {
			response.statusCode = statusCode;
			return this;
		}

		public Builder errorClass(String errorClass) {
			response.errorClass = errorClass;
			return this;
		}

		public Builder errorMessage(String errorMessage) {
			response.errorMessage = errorMessage;
			return this;
		}

		public Builder errorOrigin(String errorOrigin) {
			response.errorOrigin = errorOrigin;
			return this;
		}

		public Builder payload(Object payload) {
			response.payload = payload;
			return this;
		}

		public Response build() {
			return response;
		}
	}
}