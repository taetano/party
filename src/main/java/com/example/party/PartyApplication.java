package com.example.party;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.party.category.dto.CategoryRequest;
import com.example.party.category.entity.Category;
import com.example.party.category.repository.CategoryRepository;

@SpringBootApplication
@EnableJpaAuditing
public class PartyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartyApplication.class, args);
	}

}