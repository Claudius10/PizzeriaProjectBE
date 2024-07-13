package org.pizzeria.api.entity.dto.error;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ApiErrorDTO

		(String timesStamp,
		 int statusCode,
		 String path,
		 String errorMsg) {

	private ApiErrorDTO(Builder builder) {
		this(builder.timeStamp, builder.statusCode, builder.path, builder.errorMsg);
	}

	public static class Builder {

		private final String timeStamp;

		private int statusCode;

		private String path;

		private String errorMsg;

		public Builder() {
			this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy"));
		}

		public Builder withStatusCode(int statusCode) {
			this.statusCode = statusCode;
			return this;
		}

		public Builder withPath(String path) {
			this.path = path;
			return this;
		}

		public Builder withErrorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
			return this;
		}

		public ApiErrorDTO build() {
			return new ApiErrorDTO(this);
		}
	}
}
