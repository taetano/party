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

	//카테고리 더미데이터 넣기
	@Bean
	public CommandLineRunner dummyData(CategoryRepository categoryRepository) {
		return (args -> {
			CategoryRequest categoryRequest = new CategoryRequest("음식");

			Category category = new Category(categoryRequest);

			categoryRepository.save(category);

		});
	}

}




