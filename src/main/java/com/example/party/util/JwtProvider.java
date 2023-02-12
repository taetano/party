package com.example.party.util;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;


import com.example.party.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JwtProvider {
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String BEARER_PREFIX = "Bearer";
	private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 임시로 작성해놓았습니다. 의견주시면 감사하겠습니다.
	private static final int expire = 60 * 60 * 24 * 1000;

	public static String generateToken(User user) {
		Date curDate = new Date();
		Date expireDate = new Date(curDate.getTime() + expire);

		return Jwts.builder()
			.setSubject(user.getEmail())
			.setClaims(generateClaims(user))
			.setIssuedAt(curDate)
			.setExpiration(expireDate)
			.signWith(KEY)
			.compact();
	}

	public static Optional<String> resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

		if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
			return Optional.of(bearerToken.substring(6));
		}

		return Optional.empty();
	}

	public static String getEmailFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public static boolean validationToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJwt(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("유효하지 않은 JWT 서명 입니다.");
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토큰 입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않은 JWT 토큰 입니다.");
		} catch (IllegalArgumentException e) {
			log.info("잘못된 JWT 토큰 입니다.");
		}
		return false;
	}

	private static Claims getAllClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(KEY)
			.build()
			.parseClaimsJwt(token)
			.getBody();
	}

	private static <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
		final Claims claims = getAllClaims(token);
		return claimResolver.apply(claims);
	}


	// mypage 정보를 서버까지 접근해서 줘야하나? 라는 생각을 해봤습니다.
	private static Claims generateClaims(User user) {
		Claims claims = Jwts.claims();
		claims.put("id", user.getId());
		claims.put("nickname", user.getNickname());
		claims.put("phoneNum", user.getPhoneNum());
		claims.put("profileImg", user.getProfileImg());
		claims.put("comment", user.getComment());
		claims.put("noShowCnt", user.getNoShowCnt());
		claims.put("participationCnt", user.getParticipationCnt());
		return claims;
	}
}
