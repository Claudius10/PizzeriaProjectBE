package PizzaApp.api.exceptions.errorDTO;

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

		public Builder withErrorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
			return this;
		}

		public ApiErrorDTO build() {
			return new ApiErrorDTO(this);
		}
	}
}
