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

	 @Bean
	 public CommandLineRunner dummyData(CategoryRepository categoryRepository) {
	 	return (args -> {
	 		CategoryRequest categoryRequest1 = new CategoryRequest("음식");
	 		CategoryRequest categoryRequest2 = new CategoryRequest("공부");
	 		CategoryRequest categoryRequest3 = new CategoryRequest("게임");
	 		CategoryRequest categoryRequest4 = new CategoryRequest("문화생활");

	 		Category category1 = new Category(categoryRequest1);
	 		Category category2 = new Category(categoryRequest2);
	 		Category category3 = new Category(categoryRequest3);
	 		Category category4 = new Category(categoryRequest4);

	 		categoryRepository.save(category1);
	 		categoryRepository.save(category2);
	 		categoryRepository.save(category3);
	 		categoryRepository.save(category4);

	 	});
	 }

}
