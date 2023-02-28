package com.example.party.user.service;

import com.example.party.global.common.ApiResponse;
import com.example.party.global.common.DataApiResponse;
import com.example.party.global.common.ItemApiResponse;
import com.example.party.global.exception.BadRequestException;
import com.example.party.global.exception.NotFoundException;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.repository.PartyPostRepository;
import com.example.party.restriction.dto.ReportPostResponse;
import com.example.party.restriction.dto.ReportUserResponse;
import com.example.party.restriction.entity.ReportPost;
import com.example.party.restriction.repository.ReportPostRepository;
import com.example.party.restriction.repository.ReportUserRepository;
import com.example.party.user.dto.BlackListResponse;
import com.example.party.user.entity.User;
import com.example.party.user.exception.UserNotFoundException;
import com.example.party.user.repository.UserRepository;
import com.example.party.user.type.UserRole;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class AdminService {
    private final String accountMsg = "\n계정 정책에 위반되어 삭제 처리 되었습니다\n\n\t문의는 asdfzxc@gmail.com\n";
    private final String postMsg = "\n게시글 정책에 위반되어 블랙리스트 처리되었습니다\n\n\t문의는 asdfzxc@gmail.com\n";
    private final ReportUserRepository reportUserRepository;
    private final ReportPostRepository reportPostRepository;
    private final PartyPostRepository partyPostRepository;
    private final UserRepository userRepository;

    //유저 신고 로그 조회
    public DataApiResponse<ReportUserResponse> findReportUserList(int page) {
        Pageable pageable = PageRequest.of(page, 6, Sort.by("createdAt").descending());
        List<ReportUserResponse> reportUserResponseList = reportUserRepository.findAllByOrderById(pageable).stream()
                .map(ReportUserResponse::new).collect(Collectors.toList());
        return DataApiResponse.ok("유저 신고 로그 조회 완료", reportUserResponseList);
    }

    //모집글 신고 로그 조회
    public DataApiResponse<ReportPostResponse> findReportPostList(int page) {
        Pageable pageable = PageRequest.of(page, 6, Sort.by("createdAt").descending());
        List<ReportPostResponse> reportPostResponseList = reportPostRepository.findAllByOrderById(pageable).stream()
                .map(ReportPostResponse::new).collect(Collectors.toList());
        return DataApiResponse.ok("유저 신고 로그 조회 완료", reportPostResponseList);
    }

    //노쇼 로그 조회
    public DataApiResponse<?> findNoShowList(User user) {
//        checkAdmin(user);
//        Pageable 적용 방법 찾아야함
        List<User> users = userRepository.findAllByNoShowList();
        return DataApiResponse.ok("노쇼 로그 조회 완료", users );
    }

    //모집글 삭제
    public ApiResponse deletePost(User user, Long partyPostId) {
        checkAdmin(user);

        PartyPost partyPost = partyPostRepository.findById(partyPostId)
                .orElseThrow(NotFoundException::new);
        List<ReportPost> reportPosts = reportPostRepository.findAllByPartyPostId(partyPost.getId());
        //나중에 다시 정해야함 기준이 없어서 일단 랜덤 사용

        User createPostUser = partyPost.getUser();
        createPostUser.getProfile().plusAdminReportCnt();
        if (createPostUser.getProfile().getAdminReportCnt() >= 3) {
            createPostUser.setSuspended();
            reportPostRepository.deleteAll(reportPosts);
            partyPostRepository.delete(partyPost);
            return ApiResponse.ok("삭제 및 블랙리스트 처리 완료");
        }

        reportPostRepository.deleteAll(reportPosts);
        partyPostRepository.delete(partyPost);
        return ApiResponse.ok("게시글 삭제 완료");
    }

    //회원 블랙리스트 등록
    public ApiResponse setSuspended(User user, Long userId) {
        checkAdmin(user);
        isMySelf(user, userId);
        User blackuser = findByUser(userId);
        blackuser.setSuspended();

        List<PartyPost> partyPost = partyPostRepository.findAllByUserId(blackuser.getId());
        if (!partyPost.isEmpty()) {
            partyPostRepository.deleteAll(partyPost);
        }

        userRepository.save(blackuser);
        return ApiResponse.ok("블랙리스트 해제 완료");
    }

    //회원 블랙리스트 해제
    public ApiResponse setActive(User user, Long userId) {
        checkAdmin(user);
        isMySelf(user, userId);
        User userIf = findByUser(userId);
        userIf.setActive();
        userRepository.save(userIf);
        return ApiResponse.ok("블랙리스트 해제 완료");
    }

    //블랙리스트 조회
    public DataApiResponse<BlackListResponse> getBlackList(User user) {
        checkAdmin(user);
        List<BlackListResponse> blackList = userRepository.statusEqualSuspended().stream()
                .map(b -> new BlackListResponse(b,accountMsg)).collect(Collectors.toList());
        return DataApiResponse.ok("블랙리스트 조회 완료", blackList);
    }

    //private
    private void checkAdmin(User user) {
        if (!user.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalArgumentException("권한 없음");
        }
    }

    private User findByUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private void isMySelf(User users, Long blackedId) {
        if (users.getId().equals(blackedId))
            throw new BadRequestException("관리자 입니다");
    }
}