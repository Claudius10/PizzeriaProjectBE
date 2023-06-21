package PizzaApp.api.entity.user.dto;

public class AuthDTO {
	private String accessToken;
	private String refreshToken;

	private AuthDTO(Builder builder) {
		this.accessToken = builder.accessToken;
		this.refreshToken = builder.refreshToken;
	}

	public static class Builder {
		private String accessToken;
		private String refreshToken;

		public Builder() {
		}

		public Builder withAccessToken(String accessToken) {
			this.accessToken = accessToken;
			return this;
		}

		public Builder withRefreshToken(String refreshToken) {
			this.refreshToken = refreshToken;
			return this;
		}

		public AuthDTO build() {
			return new AuthDTO(this);
		}
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
