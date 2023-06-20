package PizzaApp.api.entity.user.dto;

public class AuthDTO {

	private Long userId;
	private String username;
	private String accessToken;
	private String refreshToken;

	private AuthDTO(Builder builder) {
		this.userId = builder.userId;
		this.username = builder.username;
		this.accessToken = builder.accessToken;
		this.refreshToken = builder.refreshToken;
	}

	public static class Builder {
		private Long userId;
		private String username;
		private String accessToken;
		private String refreshToken;

		public Builder() {
		}

		public Builder withUserId(Long userId) {
			this.userId = userId;
			return this;
		}

		public Builder withUsername(String username) {
			this.username = username;
			return this;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
