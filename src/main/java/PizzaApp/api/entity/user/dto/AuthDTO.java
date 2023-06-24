package PizzaApp.api.entity.user.dto;

public class AuthDTO {
	private String accessToken, refreshToken, username;
	private Long userId;

	private AuthDTO(Builder builder) {
		this.accessToken = builder.accessToken;
		this.refreshToken = builder.refreshToken;
		this.username = builder.username;
		this.userId = builder.userId;
	}

	public static class Builder {
		private String accessToken, refreshToken, username;
		private Long userId;

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

		public Builder withUsername(String username) {
			this.username = username;
			return this;
		}

		public Builder withUserId(Long userId) {
			this.userId = userId;
			return this;
		}

		public AuthDTO build() {
			return new AuthDTO(this);
		}
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public String getUsername() {
		return username;
	}

	public Long getUserId() {
		return userId;
	}
}
