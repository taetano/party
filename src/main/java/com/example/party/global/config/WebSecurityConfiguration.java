package com.example.party.global.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.party.global.security.JwtAccessDeniedHandler;
import com.example.party.global.security.JwtEntryPoint;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfiguration {
	private final UserDetailsService userDetailsService;

	@Bean
	public JwtEntryPoint entryPoint() {
		return new JwtEntryPoint();
	}

	@Bean
	public JwtAccessDeniedHandler accessDeniedHandler() {
		return new JwtAccessDeniedHandler();
	}

	@Bean
	// public JwtVerificationFilter jwtVerificationFilter() {
	// 	return new JwtVerificationFilter(userDetailsService);
	// }

	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
			.requestMatchers(PathRequest.toH2Console())
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			// .addFilterBefore(jwtVerificationFilter(), UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(config -> config
				.authenticationEntryPoint(entryPoint())
				.accessDeniedHandler(accessDeniedHandler())
			);

		http.authorizeHttpRequests(auth -> auth
			.anyRequest().permitAll()
		);

		return http.build();
	}
}
