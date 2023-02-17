package com.example.party;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling //Spring scheduler 기능 추가
@SpringBootApplication
public class PartyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartyApplication.class, args);
	}
}
