package com.example.party.user.controller;

import com.example.party.global.common.DataApiResponse;
import com.example.party.global.common.ItemApiResponse;
import com.example.party.restriction.dto.ReportPostResponse;
import com.example.party.restriction.dto.ReportUserResponse;
import com.example.party.user.dto.AdminResponse;
import com.example.party.user.entity.User;
import com.example.party.user.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    //유저 신고 로그 조회
    @GetMapping("/report/users")
    public ResponseEntity<DataApiResponse<ReportUserResponse>> findReportUserList(@RequestParam int page) {
        return ResponseEntity.ok(adminService.findReportUserList(page - 1));
    }

    //모집글 신고 로그 조회
    @GetMapping("/report/party-posts")
    public ResponseEntity<DataApiResponse<ReportPostResponse>> findReportPostList(@RequestParam int page) {
        return ResponseEntity.ok(adminService.findReportPostList(page - 1));
    }

    //모집글 삭제
    @DeleteMapping("/party-posts/{partyPostId}/reportPost/{reportPostId}")
    public ResponseEntity<ItemApiResponse<AdminResponse>> deletePost(@AuthenticationPrincipal User user, @PathVariable Long partyPostId) {
        return ResponseEntity.ok(adminService.deletePost(user, partyPostId));
    }

    //블랙리스트 등록
    @PostMapping("/blackList/suspended/{userId}")
    public ResponseEntity<ItemApiResponse<AdminResponse>> setSuspended(@AuthenticationPrincipal User user, @PathVariable Long userId) {
        return ResponseEntity.ok(adminService.setSuspended(user, userId));
    }

    //블랙리스트 해제
    @PostMapping("/blackList/active/{userId}")
    public ResponseEntity<?> setActive(@AuthenticationPrincipal User user, @PathVariable Long userId) {
        return ResponseEntity.ok(adminService.setActive(user, userId));
    }

    //블랙리스트 조회
    @GetMapping("/blackList")
    private ResponseEntity<DataApiResponse<?>> getBlackList(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(adminService.getBlackList(user));
    }
}
