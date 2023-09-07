package PizzaApp.api.entity.dto.misc;

public class AuthDTO {
	private final String accessToken;
	private final String refreshToken;
	private final String name;
	private final String email;
	private final Long userId;

	private AuthDTO(Builder builder) {
		this.userId = builder.userId;
		this.name = builder.name;
		this.email = builder.email;
		this.accessToken = builder.accessToken;
		this.refreshToken = builder.refreshToken;
	}

	public static class Builder {
		private String accessToken, refreshToken, name, email;
		private Long userId;

		public Builder() {
		}

		public Builder withUserId(Long userId) {
			this.userId = userId;
			return this;
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withEmail(String email) {
			this.email = email;
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

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public Long getUserId() {
		return userId;
	}
}
