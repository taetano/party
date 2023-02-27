package com.example.party;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class PartyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartyApplication.class, args);
	}

	// @Bean
	// public CommandLineRunner dummyData(
	// 	CategoryRepository categoryRepository, UserRepository userRepository, PasswordEncoder passwordEncoder,
	// 	ProfilesRepository profilesRepository, PartyPostRepository partyPostRepository, PartyRepository partyRepository,
	// 	NoShowRepository noShowRepository
	// ) {
	// 	return (args -> {
	// 		CategoryRequest categoryRequest1 = new CategoryRequest("음식");
	// 		CategoryRequest categoryRequest2 = new CategoryRequest("공부");
	// 		CategoryRequest categoryRequest3 = new CategoryRequest("게임");
	// 		CategoryRequest categoryRequest4 = new CategoryRequest("문화생활");
	//
	// 		Category category1 = new Category(categoryRequest1);
	// 		Category category2 = new Category(categoryRequest2);
	// 		Category category3 = new Category(categoryRequest3);
	// 		Category category4 = new Category(categoryRequest4);
	//
	// 		categoryRepository.save(category1);
	// 		categoryRepository.save(category2);
	// 		categoryRepository.save(category3);
	// 		categoryRepository.save(category4);
	//
	// 		SignupRequest signupRequest0 = SignupRequest.builder().email("asdfzxc@gmail.com")
	// 			.password("asdf!1234").nickname("운영자").phoneNum("010-0000-0000").build();
	//
	// 		SignupRequest signupRequest1 = SignupRequest.builder().email("zxc112345@gmail.com")
	// 			.password("asdf!1234").nickname("게임일").phoneNum("010-0000-0000").build();
	// 		SignupRequest signupRequest2 = SignupRequest.builder().email("zxc122345@gmail.com")
	// 			.password("asdf!1234").nickname("게임이").phoneNum("010-0000-0000").build();
	// 		SignupRequest signupRequest3 = SignupRequest.builder().email("zxc123345@gmail.com")
	// 			.password("asdf!1234").nickname("게임삼").phoneNum("010-0000-0000").build();
	// 		SignupRequest signupRequest4 = SignupRequest.builder().email("zxc123445@gmail.com")
	// 			.password("asdf!1234").nickname("게임사").phoneNum("010-0000-0000").build();
	// 		SignupRequest signupRequest5 = SignupRequest.builder().email("zxc123455@gmail.com")
	// 			.password("asdf!1234").nickname("게임오").phoneNum("010-0000-0000").build();
	//
	// 		//다른 모집글 작성, 파티장
	// 		SignupRequest signupRequest6 = SignupRequest.builder().email("zxc56789@gmail.com")
	// 			.password("asdf!1234").nickname("맛집일").phoneNum("010-0000-0000").build();
	//
	// 		String password = passwordEncoder.encode(signupRequest1.getPassword());
	// 		Profile profile0 = new Profile();
	// 		Profile profile1 = new Profile();
	// 		Profile profile2 = new Profile();
	// 		Profile profile3 = new Profile();
	// 		Profile profile4 = new Profile();
	// 		Profile profile5 = new Profile();
	// 		Profile profile6 = new Profile();
	//
	// 		//            profilesRepository.save(profile1);
	// 		//            profilesRepository.save(profile2);
	// 		//            profilesRepository.save(profile3);
	// 		//            profilesRepository.save(profile4);
	// 		//            profilesRepository.save(profile5);
	// 		//            profilesRepository.save(profile6);
	//
	// 		User Admin = new User(signupRequest0, password, profile0);
	// 		User user1 = new User(signupRequest1, password, profile1);
	// 		User user2 = new User(signupRequest2, password, profile2);
	// 		User user3 = new User(signupRequest3, password, profile3);
	// 		User user4 = new User(signupRequest4, password, profile4);
	// 		User user5 = new User(signupRequest5, password, profile5);
	// 		User user6 = new User(signupRequest6, password, profile6);
	//
	// 		Admin.changeAdmin();
	//
	// 		userRepository.save(Admin);
	// 		userRepository.save(user1);
	// 		userRepository.save(user2);
	// 		userRepository.save(user3);
	// 		userRepository.save(user4);
	// 		userRepository.save(user5);
	// 		userRepository.save(user6);
	//
	// 		PartyPostRequest partyPostRequest1 = PartyPostRequest.builder().title("LOL 자유랭크").content("다이아 이상")
	// 			.categoryId((long)3).maxMember((byte)5).partyDate("2023-02-23 15:40").partyAddress("onLine")
	// 			.partyPlace("onLine").build();
	// 		PartyPostRequest partyPostRequest2 = PartyPostRequest.builder().title("맛있는 녀석들").content("햄최 3개 이상")
	// 			.categoryId((long)1).maxMember((byte)4).partyDate("2023-02-25 15:40").partyAddress("전국")
	// 			.partyPlace("맛집").build();
	//
	// 		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	// 		LocalDateTime partyDate = LocalDateTime.parse(partyPostRequest1.getPartyDate(), formatter);
	//
	// 		PartyPost partyPost1 = new PartyPost(user1, partyPostRequest1, partyDate, category3);
	// 		PartyPost partyPost2 = new PartyPost(user6, partyPostRequest2, partyDate, category1);
	//
	// 		//노쇼 신고시
	// 		//          partyPost1.changeStatusNoShow();
	// 		//			partyPost2.changeStatusNoShow();
	//
	// 		Party party = new Party(partyPost1);
	// 		//ACCEPT User 라는 가정
	// 		//            party.addUser(user1);
	// 		party.addUser(user2);
	// 		party.addUser(user3);
	// 		party.addUser(user4);
	// 		party.addUser(user5);
	//
	// 		partyPost1.changeParty(party);
	//
	// 		partyPostRepository.save(partyPost1);
	// 		partyPostRepository.save(partyPost2);
	//
	// 		NoShow noShow1 = new NoShow(user1, user2, partyPost1);
	// 		NoShow noShow2 = new NoShow(user3, user2, partyPost1);
	// 		NoShow noShow3 = new NoShow(user4, user2, partyPost1);
	// 		NoShow noShow4 = new NoShow(user5, user2, partyPost1);
	//
	// 		noShow1.PlusNoShowReportCnt();
	// 		noShow2.PlusNoShowReportCnt();
	// 		noShow3.PlusNoShowReportCnt();
	// 		noShow4.PlusNoShowReportCnt();
	//
	// 		noShowRepository.save(noShow1);
	// 		noShowRepository.save(noShow2);
	// 		noShowRepository.save(noShow3);
	// 		noShowRepository.save(noShow4);
	//
	// 	});
	// }
}
