package com.example.party;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class PartyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartyApplication.class, args);
	}
	//카테고리 더미데이터 넣기
	// @Bean
	// public CommandLineRunner dummyData(CategoryRepository categoryRepository, MsgRoomRepository msgRoomRepository, UserRepository userRepository) {
	// // 	return (args -> {
	// // 		CategoryRequest categoryRequest1 = new CategoryRequest("음식");
	// // 		CategoryRequest categoryRequest2 = new CategoryRequest("공부");
	// // 		CategoryRequest categoryRequest3 = new CategoryRequest("게임");
	// // 		CategoryRequest categoryRequest4 = new CategoryRequest("문화생활");
	// //
	// // 		Category category1 = new Category(categoryRequest1);
	// // 		Category category2 = new Category(categoryRequest2);
	// // 		Category category3 = new Category(categoryRequest3);
	// // 		Category category4 = new Category(categoryRequest4);
	// //
	// // 		categoryRepository.save(category1);
	// // 		categoryRepository.save(category2);
	// // 		categoryRepository.save(category3);
	// // 		categoryRepository.save(category4);
	// //
	// // 	});
	// 	return (args -> {
	// 		// System.out.println("되냐???");
	// 		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			//
			// SignupRequest signupRequest = new SignupRequest("user@eamil.com", "cksgh123!@#", "userName",
			// 	"010-1234-1234");
			// String encodedPassword = encoder.encode(signupRequest.getPassword());
			// User user1 = new User(signupRequest, encodedPassword, new Profile());

		// 	User user = userRepository.findById(2L)
		// 		.get();
		//
		// 	SignupRequest signupRequest2 = new SignupRequest("anohter@eamil.com", "cksgh123!@#", "AotherName",
		// 		"010-1234-1234");
		// 	String encodedPassword2 = encoder.encode(signupRequest2.getPassword());
		// 	User user2 = new User(signupRequest2, encodedPassword2, new Profile());
		//
		// 	MsgRoom msgRoom = new MsgRoom(user.getNickname() + user2.getNickname(), user, user2);
		// 	msgRoomRepository.save(msgRoom);
		// });
	// }
}