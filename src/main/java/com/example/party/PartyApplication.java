package com.example.party;

import com.example.party.category.exception.CategoryNotFoundException;
import com.example.party.partypost.dto.PartyPostRequest;
import com.example.party.partypost.entity.Parties;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.repository.PartyPostRepository;
import com.example.party.partypost.repository.PartyRepository;
import com.example.party.restriction.entity.NoShow;
import com.example.party.restriction.repository.NoShowRepository;
import com.example.party.user.dto.SignupRequest;
import com.example.party.user.entity.Profiles;
import com.example.party.user.entity.User;
import com.example.party.user.repository.ProfileRepository;
import com.example.party.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.party.category.dto.CategoryRequest;
import com.example.party.category.entity.Category;
import com.example.party.category.repository.CategoryRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
public class PartyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartyApplication.class, args);
	}

	 @Bean
	 public CommandLineRunner dummyData(
			 CategoryRepository categoryRepository, UserRepository userRepository, PasswordEncoder passwordEncoder,
			 ProfileRepository profileRepository, PartyPostRepository partyPostRepository, PartyRepository partyRepository,
			 NoShowRepository noShowRepository
	 ) {
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

			SignupRequest signupRequest1 = SignupRequest.builder().email("zxc112345@gmail.com")
					.password("asdf!1234").nickname("게임일").phoneNum("010-0000-0000").build();
			SignupRequest signupRequest2 = SignupRequest.builder().email("zxc122345@gmail.com")
					.password("asdf!1234").nickname("게임이").phoneNum("010-0000-0000").build();
			SignupRequest signupRequest3 = SignupRequest.builder().email("zxc123345@gmail.com")
					.password("asdf!1234").nickname("게임삼").phoneNum("010-0000-0000").build();
			SignupRequest signupRequest4 = SignupRequest.builder().email("zxc123445@gmail.com")
					.password("asdf!1234").nickname("게임사").phoneNum("010-0000-0000").build();
			SignupRequest signupRequest5 = SignupRequest.builder().email("zxc123455@gmail.com")
					.password("asdf!1234").nickname("게임오").phoneNum("010-0000-0000").build();

			//다른 모집글 작성, 파티장
			SignupRequest signupRequest6 = SignupRequest.builder().email("zxc56789@gmail.com")
					.password("asdf!1234").nickname("맛집일").phoneNum("010-0000-0000").build();

			String password = passwordEncoder.encode(signupRequest1.getPassword());
			Profiles profiles1 = new Profiles();
			Profiles profiles2 = new Profiles();
			Profiles profiles3 = new Profiles();
			Profiles profiles4 = new Profiles();
			Profiles profiles5 = new Profiles();
			Profiles profiles6 = new Profiles();

			profileRepository.save(profiles1);
			profileRepository.save(profiles2);
			profileRepository.save(profiles3);
			profileRepository.save(profiles4);
			profileRepository.save(profiles5);
			profileRepository.save(profiles6);

			User user1 = new User(signupRequest1, password, profiles1);
			User user2 = new User(signupRequest2,password, profiles2);
			User user3 = new User(signupRequest3, password, profiles3);
			User user4 = new User(signupRequest4,password, profiles4);
			User user5 = new User(signupRequest5, password, profiles5);
			User user6 = new User(signupRequest6, password, profiles6);


			userRepository.save(user1);
			userRepository.save(user2);
			userRepository.save(user3);
			userRepository.save(user4);
			userRepository.save(user5);
			userRepository.save(user6);

			PartyPostRequest partyPostRequest1 = PartyPostRequest.builder().title("LOL 자유랭크").content("다이아 이상")
					.categoryId((long) 3).maxMember((byte) 5).partyDate("2023-02-23 15:40").partyAddress("onLine")
					.partyPlace("onLine").build();
			PartyPostRequest partyPostRequest2 = PartyPostRequest.builder().title("맛있는 녀석들").content("햄최 3개 이상")
					.categoryId((long) 1).maxMember((byte) 4).partyDate("2023-02-23 15:40").partyAddress("전국")
					.partyPlace("맛집").build();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime partyDate = LocalDateTime.parse(partyPostRequest1.getPartyDate(), formatter);

			PartyPost partyPost1 = new PartyPost(user1, partyPostRequest1, partyDate, category3);
			PartyPost partyPost2 = new PartyPost(user6, partyPostRequest2, partyDate, category1);

			partyPost1.changeStatusNoShow();
//			partyPost2.changeStatusNoShow();

			partyPostRepository.save(partyPost1);
			partyPostRepository.save(partyPost2);

			Parties parties = new Parties(partyPost1);
			//ACCEPT User
			parties.addUsers(user1);
			parties.addUsers(user2);
			parties.addUsers(user3);
			parties.addUsers(user4);
			parties.addUsers(user5);

			partyRepository.save(parties);

			NoShow noShow1 = new NoShow(user1, user2, partyPost1);
			NoShow noShow2 = new NoShow(user3, user2, partyPost1);
			NoShow noShow3 = new NoShow(user4, user2, partyPost1);
			NoShow noShow4 = new NoShow(user5, user2, partyPost1);

			noShow1.PlusNoShowReportCnt();
			noShow2.PlusNoShowReportCnt();
			noShow3.PlusNoShowReportCnt();
			noShow4.PlusNoShowReportCnt();

			noShowRepository.save(noShow1);
			noShowRepository.save(noShow2);
			noShowRepository.save(noShow3);
			noShowRepository.save(noShow4);

	 	});
	 }
}
