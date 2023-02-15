// package com.example.party.global.security;
//
// import java.io.IOException;
//
// import javax.servlet.FilterChain;
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
//
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.http.HttpStatus;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.util.ObjectUtils;
// import org.springframework.web.filter.OncePerRequestFilter;
//
// import com.example.party.global.type.JwtEnum;
// import com.example.party.util.JwtProvider;
//
// import io.jsonwebtoken.Jwt;
// import lombok.RequiredArgsConstructor;
//
// @RequiredArgsConstructor
// public class JwtVerificationFilter extends OncePerRequestFilter {
// 	private final UserDetailsService userDetailsService;
// 	private final RedisTemplate redisTemplate;
//
// 	@Override // 로그인시 referer 사용 고려
// 	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
// 		FilterChain filterChain) throws ServletException, IOException {
//
// 		String token = JwtProvider.resolveToken(request);
// 		if (token != null) {
// 			if (JwtProvider.validationToken(token) == JwtEnum.DENIED){
// 				new IllegalArgumentException("토큰 만료");
// 				return;
// 		} else if (JwtProvider.validationToken(token) == JwtEnum.EXPIRED) {
//
// 			}
// 			if (JwtProvider.validationToken())
// 			// 유효한 토큰인지 확인합니다. -> validation 진행
// 		if (token != null && JwtProvider.validationToken(token)) {
// 			// Redis에 해당 accessToken logout 여부를 확인
// 			String isLogout = (String)redisTemplate.opsForValue().get(token);
//
// 			// 로그아웃이 없는(되어 있지 않은) 경우 해당 토큰은 정상적으로 작동하기
// 			if (ObjectUtils.isEmpty(isLogout)) {
//
// 				// 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
// 				Authentication authentication = JwtProvider.getAuthentication(token);
// 				// SecurityContext 에 Authentication 객체를 저장합니다.
// 				SecurityContextHolder.getContext().setAuthentication(authentication);
//
// 			}
// 		}
// 		filterChain.doFilter(request, response);
// 	}
//
// 	private void setAuthentication(String email) {
// 		UserDetails user = userDetailsService.loadUserByUsername(email);
// 		Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
// 		SecurityContextHolder.createEmptyContext().setAuthentication(authentication);
// 	}
// }
