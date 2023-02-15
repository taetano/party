package com.example.party.util;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class redisUtil {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	public void redisSetTest(String key, Object data) {
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		vop.set(key, data);
		// 만료 제한 60초
		vop.set(key, data, Duration.ofSeconds(60));
	}

	public Object redisGetTest(String key) {
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		return vop.get("key1");
	}
}
