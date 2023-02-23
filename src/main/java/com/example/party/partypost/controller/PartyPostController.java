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

import com.example.party.partypost.dto.MyPartyPostListResponse;
import com.example.party.partypost.dto.PartyPostListResponse;
import com.example.party.partypost.dto.PartyPostRequest;
import com.example.party.partypost.dto.PartyPostResponse;
import com.example.party.partypost.dto.UpdatePartyPostRequest;
import com.example.party.partypost.service.PartyPostService;
import com.example.party.global.common.ApiResponse;
import com.example.party.global.common.DataApiResponse;
import com.example.party.global.common.ItemApiResponse;
import com.example.party.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/party-posts")
public class PartyPostController {

    private final PartyPostService partyPostService;

    //모집글 작성
    @PostMapping("")
    public ResponseEntity<ApiResponse> createPartyPost(
            @RequestBody PartyPostRequest request, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(partyPostService.createPartyPost(user, request));
    }

    //모집글 수정
    @PatchMapping("/{party-postId}")
    public ResponseEntity<ApiResponse> updatePartyPost(
            @PathVariable(name = "party-postId") Long partyPostId, @RequestBody UpdatePartyPostRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(partyPostService.updatePartyPost(partyPostId, request, user));
    }

    //내가 작성한 모집글 리스트 조회
    @GetMapping("/mylist")
    public ResponseEntity<DataApiResponse<MyPartyPostListResponse>> findMyCreatedPartyList(
            @AuthenticationPrincipal User user, @RequestParam(name = "page", defaultValue = "1") int page) {
        return ResponseEntity.ok(partyPostService.findMyCreatedPartyList(user, page));
    }

    //내가 신청한 모집글 리스트 조회
    @GetMapping("/my-join-list")
    public ResponseEntity<DataApiResponse<MyPartyPostListResponse>> findMyJoinedPartyList(
            @AuthenticationPrincipal User user, @RequestParam(name = "page", defaultValue = "1") int page) {
        return ResponseEntity.ok(partyPostService.findMyJoinedPartyList(user, page));
    }

    //모집게시물 좋아요 (*좋아요 취소도 포함되는 기능임)
    @PostMapping("/{party-postId}/likes")
    public ResponseEntity<ApiResponse> toggleLikePartyPost(@PathVariable(name = "party-postId") Long partyPostId,
                                                           @AuthenticationPrincipal User user) {
        //좋아요 기능
        return ResponseEntity.ok(partyPostService.toggleLikePartyPost(partyPostId, user));
    }

    //모집글전체조회
    @GetMapping()
    public ResponseEntity<DataApiResponse<PartyPostListResponse>> findPartyList(
            @RequestParam int page) {
        return ResponseEntity.ok(partyPostService.findPartyList(page - 1));
    }

    //모집글 상세 조회(개별 상세조회)
    @GetMapping("/{party-postId}")
    public ResponseEntity<ItemApiResponse<PartyPostResponse>> getPartyPost(
            @PathVariable(name = "party-postId") Long partyPostId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(partyPostService.getPartyPost(partyPostId, user));
    }

    //모집글 삭제
    @DeleteMapping("/{party-postId}")
    public ResponseEntity<ApiResponse> deletePartyPost(@PathVariable(name = "party-postId") Long partyPostId,
                                                       @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(partyPostService.deletePartyPost(partyPostId, user));
    }

    // 모집글 검색 (지역명 & 제목)
    @GetMapping("/search")
    public DataApiResponse<PartyPostListResponse> searchPartyPost(
            @RequestParam(name = "searchText") String searchText,
            @RequestParam(name = "page", defaultValue = "1") int page) {
        return partyPostService.searchPartyPost(searchText, page);
    }

    //조회수 많은 핫한 모집글 조회
    @GetMapping("/hot")
    public DataApiResponse<PartyPostListResponse> findHotPartyPost() {
        return partyPostService.findHotPartyPost();
    }

    //카테고리명 별로 모집글 조회
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<DataApiResponse<PartyPostListResponse>> searchPartyPostByCategory(
            @PathVariable Long categoryId, @RequestParam(name = "page", defaultValue = "1") int page) {
        return ResponseEntity.ok(partyPostService.searchPartyPostByCategory(categoryId, page));
    }

    //가까운 모집글 조회
    @GetMapping("/near")
    public DataApiResponse<PartyPostListResponse> findNearPartyPost(
            @RequestBody String Address) {
        return partyPostService.findNearPartyPost(Address);
    }
}
