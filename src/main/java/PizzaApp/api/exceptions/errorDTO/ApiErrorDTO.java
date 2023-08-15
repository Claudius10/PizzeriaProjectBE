package PizzaApp.api.exceptions.errorDTO;

import java.util.ArrayList;
import java.util.List;

public record ApiErrorDTO

		(String timesStamp,
		 int statusCode,
		 String path,
		 boolean accessExpired,
		 List<String> errors) {

	private ApiErrorDTO(Builder builder) {
		this(builder.timeStamp, builder.statusCode, builder.path, builder.accessExpired, builder.errors);
	}

	public static class Builder {

		private final String timeStamp;

		private int statusCode;

		private String path;

		private boolean accessExpired;

		private List<String> errors;

		public Builder(String timeStamp) {
			if (timeStamp == null) {
				throw new IllegalStateException("Timestamp cannot be null.");
			}
			this.timeStamp = timeStamp;
		}

		public Builder withStatusCode(int statusCode) {
			this.statusCode = statusCode;
			return this;
		}

		public Builder withPath(String path) {
			this.path = path;
			return this;
		}

		public Builder withAccessExpired(boolean result) {
			this.accessExpired = result;
			return this;
		}

		public Builder withErrors(List<String> errors) {
			this.errors = new ArrayList<>(errors);
			return this;
		}

		public ApiErrorDTO build() {
			return new ApiErrorDTO(this);
		}
	}
}
