package com.example.party.redis;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RedisService {
	public static final String RT_TOKEN = "rTKey";
	private final RedisTemplate<String, String> redisTemplate;
	private ValueOperations<String, String> valueOperations;

	@PostConstruct
	public void setup() {
		this.valueOperations = redisTemplate.opsForValue();
	}

	public boolean existsRefreshToken(Long userId) {
		Optional<String> refreshToken = getRefreshToken(userId);
		return refreshToken.isPresent();
	}

	public void deleteRefreshToken(Long userId) {
		redisTemplate.delete(RT_TOKEN + userId);
	}

	public void putRefreshToken(Long userId, String refreshToken) {
		valueOperations.set(RT_TOKEN + userId, refreshToken);
	}

	public Optional<String> getRefreshToken(Long userId) {
		return Optional.ofNullable(valueOperations.get(RT_TOKEN + userId));
	}

}
