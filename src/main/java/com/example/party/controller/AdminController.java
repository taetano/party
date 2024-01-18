package com.example.party.controller;

import com.example.party.common.ApiResponse;
import com.example.party.common.DataApiResponse;
import com.example.party.dto.request.NoShowRequest;
import com.example.party.dto.response.ReportPostResponse;
import com.example.party.dto.response.ReportUserResponse;
import com.example.party.dto.response.BlackListResponse;
import com.example.party.dto.response.NoShowResponse;
import com.example.party.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    //노쇼 로그 조회
    @GetMapping("/report/noShow")
    public ResponseEntity<DataApiResponse<NoShowResponse>> findNoShowList(@RequestParam int page) {
        return ResponseEntity.ok(adminService.findNoShowList(page -1));
    }

    //노쇼 횟수 차감
    @PostMapping("/report/noShow")
    private ResponseEntity<ApiResponse> setNoShowCnt(@RequestBody NoShowRequest request) {
        return ResponseEntity.ok(adminService.setNoShowCnt(request));
    }

    //모집글 삭제
    @DeleteMapping("/party-posts/{partyPostId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long partyPostId) {
        return ResponseEntity.ok(adminService.deletePost(partyPostId));
    }

    //블랙리스트 등록
    @PostMapping("/blackList/suspended/{userId}")
    public ResponseEntity<ApiResponse> setSuspended(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.setSuspended(userId));
    }

    //블랙리스트 해제
    @PostMapping("/blackList/active/{userId}")
    public ResponseEntity<ApiResponse> setActive(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.setActive(userId));
    }

    //블랙리스트 조회
    @GetMapping("/blackList")
    private ResponseEntity<DataApiResponse<BlackListResponse>> getBlackList(@RequestParam int page) {
        return ResponseEntity.ok(adminService.getBlackList(page -1));
    }
}
