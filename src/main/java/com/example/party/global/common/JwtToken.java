package com.example.party.global.common;

import com.example.party.global.util.JwtProvider;
import lombok.Getter;

@Getter
public class JwtToken {
	private final String accessToken;
	private final String refreshToken;

	public JwtToken(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public static JwtToken of(Long id, String email) {
		return new JwtToken(
				JwtProvider.generateToken(email, id, JwtProvider.expire),
				JwtProvider.generateToken(email, id, JwtProvider.refreshExpire)
		);
	}
}
