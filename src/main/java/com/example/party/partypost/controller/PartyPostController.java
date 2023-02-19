package com.example.party.partypost.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.partypost.dto.MyPartyPostListResponse;
import com.example.party.partypost.dto.PartyPostListResponse;
import com.example.party.partypost.dto.PartyPostRequest;
import com.example.party.partypost.dto.PartyPostResponse;
import com.example.party.partypost.dto.SearchPartyPostListResponse;
import com.example.party.partypost.dto.UpdatePartyPostRequest;
import com.example.party.partypost.service.PartyPostService;
import com.example.party.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/party-posts")
public class PartyPostController {

	private final PartyPostService partyPostService;

	//모집글 작성
	@PostMapping("")
	public ResponseEntity<DataResponseDto<PartyPostResponse>> createPartyPost(
		@RequestBody PartyPostRequest request, @AuthenticationPrincipal User user) {
		return ResponseEntity.ok(partyPostService.createPartyPost(user, request));
	}

	//모집글 수정
	@PatchMapping("/{party-postId}")
	public ResponseEntity<DataResponseDto<PartyPostResponse>> updatePartyPost(
		@PathVariable(name = "party-postId") Long partyPostId, @RequestBody UpdatePartyPostRequest request,
		@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(partyPostService.updatePartyPost(partyPostId, request, user));
	}

	//내가 작성한 모집글 리스트 조회
	@GetMapping("/mylist")
	public ResponseEntity<ListResponseDto<MyPartyPostListResponse>> findMyCreatedPartyList(
		@AuthenticationPrincipal User user, @RequestParam int page) {
		return ResponseEntity.ok(partyPostService.findMyCreatedPartyList(user, page));
	}

	//내가 신청한 모집글 리스트 조회
	@GetMapping("/my-join-list")
	public ResponseEntity<ListResponseDto<MyPartyPostListResponse>> findMyJoinedPartyList(
		@AuthenticationPrincipal User user, @RequestParam int page) {
		return ResponseEntity.ok(partyPostService.findMyJoinedPartyList(user, page));
	}

	//모집게시물 좋아요 (*좋아요 취소도 포함되는 기능임)
	@PostMapping("/{party-postId}/likes")
	public DataResponseDto<String> toggleLikePartyPost(@PathVariable(name = "party-postId") Long partyPostId,
		@AuthenticationPrincipal User user) {
		//좋아요 기능
		return partyPostService.toggleLikePartyPost(partyPostId, user);
	}

	//모집글전체조회
	@GetMapping()
	public ResponseEntity<ListResponseDto<PartyPostListResponse>> findPartyList(
		@RequestParam int page
	) {
		return ResponseEntity.ok(partyPostService.findPartyList(page - 1));
	}

	//모집글 상세 조회(개별 상세조회)
	@GetMapping("/{party-postId}")
	public ResponseEntity<DataResponseDto<PartyPostResponse>> getPartyPost(
		@PathVariable(name = "party-postId") Long partyPostId,
		@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(partyPostService.getPartyPost(partyPostId, user));
	}

	//모집글 삭제
	@DeleteMapping("/{party-postId}")
	public ResponseEntity<ResponseDto> deletePartyPost(@PathVariable(name = "party-postId") Long partyPostId,
		@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(partyPostService.deletePartyPost(partyPostId, user));
	}

	// 모집글 검색
	@GetMapping("/search/{postsearchText}")
	public ListResponseDto<SearchPartyPostListResponse> searchPartyPost(
		@PathVariable(name = "postsearchText") String SearchText,
		@RequestParam(name = "page", defaultValue = "1") int page) {
		return partyPostService.searchPartyPost(SearchText, page);
	}

	//조회수 많은 핫한 모집글 조회
	@GetMapping("/hot")
	public ListResponseDto<SearchPartyPostListResponse> findHotPartyPost() {
		return partyPostService.findHotPartyPost();
	}

	//유저의 읍면동을 검색해서 가까운 모집글 검색
	@GetMapping("/near/{EubMyeonDong}")
	public ListResponseDto<SearchPartyPostListResponse> findNearPartyPost(
		@PathVariable(name = "EubMyeonDong") String eubMyeonDong) {
		return partyPostService.findNearPartyPost(eubMyeonDong);
	}
}
