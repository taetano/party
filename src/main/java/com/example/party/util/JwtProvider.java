package com.example.party.util;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import com.example.party.global.type.JwtEnum;
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
public class JwtProvider {
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String BEARER_PREFIX = "Bearer";
	private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 임시로 작성해놓았습니다. 의견주시면 감사하겠습니다.
	private static final int expire = 60 * 60 * 24 * 1000;
	private static final Long refreshExpire = 7 * 24 * 60 * 60 * 1000L;

	public static String generateToken(User user) {
		Date curDate = new Date();
		Date expireDate = new Date(curDate.getTime() + expire);

		return Jwts.builder()
			.setSubject(user.getEmail())
			.setIssuedAt(curDate)
			.setExpiration(expireDate)
			.signWith(KEY)
			.compact();
	}

	public static String refreshToken(User user) {
		Date curDate = new Date();
		Date RefreshExpireDate = new Date(curDate.getTime() + refreshExpire);

		return Jwts.builder()
			.setSubject(user.getEmail())
			.setIssuedAt(curDate)
			.setExpiration(RefreshExpireDate)
			.signWith(KEY)
			.compact();
	}

	public static String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

		if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(7);
		}

		return null;
	}

	public static String getEmailFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public static JwtEnum validationToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJwt(token);
			return JwtEnum.ACCESS;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("유효하지 않은 JWT 서명 입니다.");
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토큰 입니다.");
			return JwtEnum.EXPIRED;
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않은 JWT 토큰 입니다.");
		} catch (IllegalArgumentException e) {
			log.info("잘못된 JWT 토큰 입니다.");
		}
		return JwtEnum.DENIED;
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

	// accessToken 남은 유효시간
	public Long getExpiration(String accessToken) {

		Date expiration = Jwts.parserBuilder()
			.setSigningKey(KEY)
			.build()
			.parseClaimsJws(accessToken)
			.getBody()
			.getExpiration();

		// 현재 시간
		Long now = new Date().getTime();
		return (expiration.getTime() - now);
	}
}
