package com.example.party.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.example.party.util.JwtProvider;
import com.example.party.redis.RedisService;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {
	private final UserDetailsService userDetailsService;
	private final RedisService redisService;

	@Override // 로그인시 referer 사용 고려
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		JwtProvider.resolveToken(request)
			.ifPresent((token) -> {
				try {

					JwtProvider.validationToken(token);
					Long userId = JwtProvider.getUserIdFromToken(token);
					if (redisService.existsRefreshToken(userId)) {
						JwtProvider.processExpire(token);
						throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
					}
					String email = JwtProvider.getEmailFromToken(token);
					setAuthentication(email);

				} catch (ExpiredJwtException eje) {
					Arrays.stream(request.getCookies())
						.filter(cookie -> Objects.equals(cookie.getName(), RedisService.RT_TOKEN))
						.findFirst()
						.ifPresent(cookie -> {
							String refreshToken = cookie.getValue();
							Long userId = JwtProvider.getUserIdFromToken(refreshToken);

							redisService.getRefreshToken(userId)
								.ifPresent(savedRefreshToken -> {
									if (!refreshToken.equals(savedRefreshToken) || JwtProvider.validateExpire(
										savedRefreshToken)) {
										redisService.deleteRefreshToken(userId);
										throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.", eje);
									}
								});

							String email = JwtProvider.getEmailFromToken(refreshToken);
							String newAccessToken = JwtProvider.accessToken(email, userId);
							response.setHeader(JwtProvider.AUTHORIZATION_HEADER, JwtProvider.BEARER_PREFIX + newAccessToken);
							setAuthentication(email);
						});
				}
			});

		filterChain.doFilter(request, response);
	}

	private void setAuthentication(String email) {
		UserDetails user = userDetailsService.loadUserByUsername(email);
		Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
