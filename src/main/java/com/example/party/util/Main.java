package com.example.party.util;

public class Main {
	public static void main(String[] args) {
		// redis 연결이 되는지 체크용
		redisUtil redisUtil = new redisUtil();

		redisUtil.redisSetTest("test", "test");
		redisUtil.redisGetTest("test");
	}
}
