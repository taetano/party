package com.example.party.partypost.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.party.application.entity.Application;
import com.example.party.application.repository.ApplicationRepository;
import com.example.party.category.entity.Category;
import com.example.party.category.exception.CategoryNotActiveException;
import com.example.party.category.exception.CategoryNotFoundException;
import com.example.party.category.repository.CategoryRepository;
import com.example.party.global.common.ApiResponse;
import com.example.party.global.common.DataApiResponse;
import com.example.party.global.common.ItemApiResponse;
import com.example.party.partypost.dto.MyPartyPostListResponse;
import com.example.party.partypost.dto.PartyPostListResponse;
import com.example.party.partypost.dto.PartyPostRequest;
import com.example.party.partypost.dto.PartyPostResponse;
import com.example.party.partypost.dto.SearchPartyPostListResponse;
import com.example.party.partypost.dto.UpdatePartyPostRequest;
import com.example.party.partypost.entity.Party;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.exception.IsNotWritterException;
import com.example.party.partypost.exception.PartyPostNotDeletableException;
import com.example.party.partypost.exception.PartyPostNotFoundException;
import com.example.party.partypost.repository.PartyPostRepository;
import com.example.party.partypost.repository.PartyRepository;
import com.example.party.restrictions.entity.Block;
import com.example.party.user.entity.User;
import com.example.party.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PartyPostService implements IPartyPostService {

	private final PartyPostRepository partyPostRepository;
	private final UserRepository userRepository;
	private final ApplicationRepository applicationRepository;
	private final CategoryRepository categoryRepository;
	private final PartyRepository partyRepository;

	//모집글 작성
	@Override
	public ItemApiResponse<PartyPostResponse> createPartyPost(User user, PartyPostRequest request) { // 인자 달라질 수 있습니다

		//예시: "2023-02-16 12:00"
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime partyDate = LocalDateTime.parse(request.getPartyDate(), formatter);
		//1. category 찾기
		Category category = categoryRepository.findById(request.getCategoryId())
			.orElseThrow(CategoryNotFoundException::new);
		//2. category 활성화 상태 확인
		if (!category.isActive()) {
			throw new CategoryNotActiveException();
		}
		//3. PartyPost 객체 생성
		PartyPost partyPost = new PartyPost(user, request, partyDate, category);
		//Party 객체 생성
		Party party = new Party(partyPost);
		party.addUsers(user);
		//4. repository 에 저장
		partyPostRepository.save(partyPost);
		//5. partyPostResponse 생성
		PartyPostResponse partyPostResponse = new PartyPostResponse(partyPost);
		//6. DataResponseDto 생성 후 return
		return new ItemApiResponse<>(201, "모집글 작성 완료", partyPostResponse);
	}

	//모집글 전체 조회
	@Override
	@Transactional
	public DataApiResponse<PartyPostListResponse> findPartyList(int page, User user) {
		// 로그인한 유저 블랙리스트
		List<Block> blockList = user.getBlocks();
		// 1.모집글 전체 불러오기 (페이지 추가)
		Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
		//2. Page<partyPost> 를 Page<PartyPostListResponse> 로 변경
		Page<PartyPost> postPage = partyPostRepository.findAllByActiveIsTrue(pageable);
		// postPage filter 적용
		List<PartyPostListResponse> filteredPosts = postPage.stream()
			.filter(post -> !blockList.contains(post.getUser().getEmail()))
			.map(PartyPostListResponse::new)
			.collect(Collectors.toList());
		// 3.ListResponseDto 생성 후 리턴
		return DataApiResponse.ok("모집글 조회 완료", filteredPosts);
	}

	//모집글 상세 조회(개별 상세조회)
	@Override
	@Transactional
	public ItemApiResponse<PartyPostResponse> getPartyPost(Long partyPostId, User user) {
		//1. partyPostId의 partyPost 를 가져오기
		PartyPost partyPost = partyPostRepository.findById(partyPostId)
			.orElseThrow(PartyPostNotFoundException::new);

		//2. 조회자!=작성자 인경우 조회한 partyPost 의 조회수 올려주기
		partyPost.increaseViewCnt(user);

		//2. PartyPostResponse Dto 생성
		PartyPostResponse partyPostResponse = new PartyPostResponse(partyPost);

		//3. DataResponseDto 생성하고 (2) 를 넣어준 후 return
		return ItemApiResponse.ok("모집글 상세 조회 완료", partyPostResponse);
	}

	//문자 검색으로 제목,지역명으로 모집글 조회
	@Override
	@Transactional
	public DataApiResponse<SearchPartyPostListResponse> searchPartyPost(String string, int page) {
		Pageable pageable = PageRequest.of(page - 1, 20);

		//1.검색 문자에 맞는 리스트 조회
		List<PartyPost> partyPostList = partyPostRepository.findByTitleContainingOrAddressContaining(string, string,
			pageable);

		List<SearchPartyPostListResponse> partyPostListResponses = partyPostList.stream()
			.map(SearchPartyPostListResponse::new).collect(Collectors.toList());

		return DataApiResponse.ok("모집글 검색 완료", partyPostListResponses);
	}

	//카테고리명 별로 모집글 조회
	@Override
	public DataApiResponse<PartyPostListResponse> searchPartyPostByCategory(Long categoryId, int page) {
		Pageable pageable = PageRequest.of(page - 1, 20);

		Category category = categoryRepository.findById(categoryId).orElseThrow(
			() -> new CategoryNotFoundException()
		);

		if (!category.isActive()) {
			throw new CategoryNotActiveException();
		}

		List<PartyPost> partyPostList = partyPostRepository.findByCategoryId(categoryId, pageable);
		List<PartyPostListResponse> partyPostListResponses = partyPostList.stream()
			.map(PartyPostListResponse::new).collect(Collectors.toList());

		return DataApiResponse.ok("모집글 검색 완료", partyPostListResponses);
	}

	@Override
	public DataApiResponse<SearchPartyPostListResponse> findHotPartyPost() {
		Pageable pageable = PageRequest.of(0, 20, Sort.by("view_cnt"));

		List<PartyPost> partyPostList = partyPostRepository.findFirst20ByOrderByViewCntDesc();

		//1.검색 문자에 맞는 리스트 조회
		// List<PartyPost> partyPostList = partyPostRepository.findByTitleContainingOrAddressContaining(string, string,
		// 	pageable);

		List<SearchPartyPostListResponse> partyPostListResponses = partyPostList.stream()
			.map(SearchPartyPostListResponse::new).collect(Collectors.toList());

		return DataApiResponse.ok("핫한 모집글 조회 완료", partyPostListResponses);
	}

	@Override
	public DataApiResponse<SearchPartyPostListResponse> findNearPartyPost(String eubMyeonDong) {
		Pageable pageable = PageRequest.of(0, 20); //페이지 갯수 지정

		//1.검색 문자에 맞는 리스트 조회
		List<PartyPost> partyPostList = partyPostRepository.findByEubMyeonDongContaining(eubMyeonDong);

		List<SearchPartyPostListResponse> partyPostListResponses = partyPostList.stream()
			.map(SearchPartyPostListResponse::new).collect(Collectors.toList());

		return DataApiResponse.ok("주변 모집글 조회 완료", partyPostListResponses);
	}

	//모집글 수정
	@Override
	public ItemApiResponse<PartyPostResponse> updatePartyPost(Long partyPostId,
		UpdatePartyPostRequest request, User user) {

		//1. PartyPost 불러오기
		PartyPost partyPost = partyPostRepository.findById(partyPostId).orElseThrow(
			PartyPostNotFoundException::new
		);

		//2. 작성자인지 확인
		if (!partyPost.isWrittenByMe(user.getId())) {
			throw new IsNotWritterException();
		}

		//3. 수정할 내용 받기
		partyPost.update(request);

		//4. partyPostResponse 생성
		PartyPostResponse partyPostResponse = new PartyPostResponse(partyPost);

		//5. DataResponseDto 생성 후 return
		return ItemApiResponse.ok("모집글 수정 완료", partyPostResponse);
	}

	//모집글 삭제
	@Override
	public ApiResponse deletePartyPost(Long partyPostId, User user) {
		//1. partyPost 객체 가져오기
		PartyPost partyPost = partyPostRepository.findById(partyPostId).orElseThrow(
			PartyPostNotFoundException::new
		);
		//2. 삭제 가능한지 확인 (작성자인지 / 모집마감전인지 / 참가신청한 모집자가 없는지)
		canDeletePartyPost(user, partyPost);

		//3. 2가 통과한 경우 삭제 진행
		partyPost.deletePartyPost();
		return ApiResponse.ok("모집글 삭제 완료");
	}

	//내가 작성한 모집글 조회 ( 내가 파티장인 경우만 )
	@Override
	public DataApiResponse<MyPartyPostListResponse> findMyCreatedPartyList(User user, int page) {
		Pageable pageable = PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.DESC, "modifiedAt"));

		//1. user가 작성한 partyPost의 리스트
		List<PartyPost> myPartyPostList = partyPostRepository.findByUserId(user.getId(), pageable);

		//2. partyPost DTO의 LIST 생성
		List<MyPartyPostListResponse> myPartyPostDtoList = myPartyPostList.stream()
			.filter(partyPost -> partyPost.getUser().getId().equals(user.getId()))
			.map(MyPartyPostListResponse::new).collect(Collectors.toList());

		//3. DataResponseDto 생성 후 return
		return DataApiResponse.ok("내가 작성한 모집글 조회 완료", myPartyPostDtoList);
	}

	//내가 신청한 모집글 조회 ( 내가 파티원인 경우만/ 파티장인 경우 제외 )
	@Override
	public DataApiResponse<MyPartyPostListResponse> findMyJoinedPartyList(User user, int page) {
		Pageable pageable = PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.DESC, "modifiedAt"));

		//1. user가 신청한 application의 리스트
		List<Application> myApplicationList = applicationRepository.findByUserId(user.getId(), pageable);

		//2. partyPost DTO의 LIST 생성
		List<MyPartyPostListResponse> myApplicationDtoList = myApplicationList.stream()
			.filter(application -> application.getUser().getId().equals(user.getId()))
			.map(Application::getPartyPost)
			.map(MyPartyPostListResponse::new).collect(Collectors.toList());

		//3. DataResponseDto 생성 후 return
		return DataApiResponse.ok("내가 참가한 모집글 조회 완료", myApplicationDtoList);
	}

	//모집게시물 좋아요 (*좋아요 취소도 포함되는 기능임)
	public ItemApiResponse<String> toggleLikePartyPost(Long partyPostId, User user) {
		//모집글 찾기
		PartyPost partyPost = partyPostRepository.findById(partyPostId).orElseThrow(
			() -> new PartyPostNotFoundException()
		);
		String partyPostTitle = partyPost.getTitle(); //모집글 제목 입력
		User userT = userRepository.save(user);
		//좋아요 확인
		if (!(userT.getLikePartyPosts().add(partyPost))) {
			userT.getLikePartyPosts().remove(partyPost);
			return new ItemApiResponse(200, "모집글 좋아요 취소 완료", partyPostTitle);
		} else {
			return new ItemApiResponse(200, "모집글 좋아요 완료", partyPostTitle);
		}
	}

	//private 메소드
	//삭제가능여부 확인
	private void canDeletePartyPost(User user, PartyPost partyPost) {
		//1. 작성자인지 확인
		if (!partyPost.isWrittenByMe(user.getId())) {
			throw new PartyPostNotDeletableException("작성자만 모집글을 삭제할 수 있습니다");
		}
		//2. 모집글이 이미 삭제 상태인지 확인
		if (!partyPost.isActive()) {
			throw new PartyPostNotDeletableException("이미 삭제처리된 모집글입니다.");
		}

		//3. 모집마감전인지 확인
		if (!partyPost.beforeCloseDate(LocalDateTime.now())) {
			throw new PartyPostNotDeletableException("모집마감시간이 지나면 모집글을 삭제할 수 없습니다");
		}
		//3. 참가신청한 모집자가 없는지 확인
		if (!partyPost.haveNoApplications()) {
			throw new PartyPostNotDeletableException("참가신청자가 있는 경우 삭제할 수 없습니다");
		}
	}

}
