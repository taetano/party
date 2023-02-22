package com.example.party.restrictions.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.party.partypost.entity.Parties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.party.user.entity.User;
import com.example.party.global.common.ApiResponse;
import com.example.party.global.common.DataApiResponse;
import com.example.party.global.common.ItemApiResponse;
import com.example.party.global.exception.BadRequestException;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.exception.PartyPostNotFoundException;
import com.example.party.partypost.repository.PartyPostRepository;
import com.example.party.partypost.repository.PartyRepository;
import com.example.party.partypost.type.Status;
import com.example.party.restrictions.dto.BlockResponse;
import com.example.party.restrictions.dto.ReportPostRequest;
import com.example.party.restrictions.dto.ReportUserRequest;
import com.example.party.restrictions.entity.Blocks;
import com.example.party.restrictions.entity.NoShow;
import com.example.party.restrictions.entity.PostReport;
import com.example.party.restrictions.entity.UserReport;
import com.example.party.restrictions.exception.CheckedBlocksException;
import com.example.party.restrictions.repository.BlockRepository;
import com.example.party.restrictions.repository.NoShowRepository;
import com.example.party.restrictions.repository.ReportPostRepository;
import com.example.party.restrictions.repository.ReportUserRepository;
import com.example.party.user.exception.UserNotFoundException;
import com.example.party.user.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class RestrictionsService {

	private final UserRepository userRepository;
	private final PartyRepository partyRepository;
	private final BlockRepository blockRepository;
	private final NoShowRepository noShowRepository;
	private final ReportUserRepository reportUserRepository;
	private final ReportPostRepository reportPostRepository;
	private final PartyPostRepository partyPostRepository;

	//차단등록
	public ItemApiResponse<BlockResponse> blockUser(Long userId, User users) {
		User blocked = findByUser(userId);
		isMySelf(users, userId);
		List<Blocks> blocks = blockRepository.findAllByBlockerId(users.getId());
		if (blocks.size() > 0) {
			for (Blocks blockIf : blocks) {
				if (Objects.equals(blockIf.getBlocked().getId(), (blocked.getId()))) {
					throw new CheckedBlocksException("이미 신고한 유저입니다");
				}
			}
		}
		Blocks block = new Blocks(users, blocked);
		blockRepository.save(block);
		return ItemApiResponse.ok("차단등록 완료", new BlockResponse(block));
	}

	//차단해제
	public ApiResponse unBlockUser(Long userId, User users) {
		User blocked = findByUser(userId);
		List<Blocks> blocks = blockRepository.findAllByBlockerId(users.getId());
		if (!blocks.isEmpty()) {
			for (Blocks block : blocks) {
				if (Objects.equals(block.getBlocked().getId(), (blocked.getId()))) {
					blockRepository.delete(block);
					return ApiResponse.ok("차단해제 완료");
				}
			}
		} else {
			throw new BadRequestException("차단한 유저가 아닙니다");
		}
		return ApiResponse.ok("차단해제 완료");
	}

	//차단목록 조회
	public DataApiResponse<BlockResponse> getBlockedList(int page, User users) {
		Pageable pageable = PageRequest.of(page, 10);
		List<Blocks> blocks = blockRepository.findAllByBlockerId(users.getId(), pageable);
		List<BlockResponse> blockResponse = blocks.stream().map(BlockResponse::new).collect(Collectors.toList());
		return DataApiResponse.ok("조회 성공", blockResponse);
	}

	//유저 신고
	public ApiResponse reportUsers(User users, ReportUserRequest request) {
		//신고할 유저
		isMySelf(users, request.getUserId());
		User reportUsers = findByUser(request.getUserId());
		if (reportUserRepository.existsByReporterIdAndReportedId(users.getId(), reportUsers.getId())) {
			throw new BadRequestException("이미 신고한 유저입니다");
		}
		UserReport userReports = new UserReport(users, reportUsers, request);
		reportUserRepository.save(userReports);
		return ApiResponse.ok("유저 신고 완료");
	}

	//모집글 신고
	public ApiResponse reportPosts(User users, ReportPostRequest request) {
		PartyPost post = partyPostRepository.findById(request.getPostId())
			.orElseThrow(PartyPostNotFoundException::new);
		if (post.getUser().equals(users)) {
			throw new BadRequestException("본인이 작성한 글입니다");
		}
		Optional<PostReport> checkPostReport = reportPostRepository
			.findByUserIdAndReportPostId(users.getId(), post.getId());
		if (checkPostReport.isPresent()) {
			throw new BadRequestException("이미 신고한 모집글입니다");
		}
		PostReport postReports = new PostReport(users, request, post);
		reportPostRepository.save(postReports);
		return ApiResponse.ok("모집글 신고 완료");
	}

	//노쇼 신고
	public ApiResponse reportNoShow(User usersDetails, Long noShowUserId) {
		//신고할 유저
		User user = findByUser(noShowUserId);
		//로그인한 유저의 파티객체
		Parties parties = partyRepository.findById(usersDetails.getId())
			.orElseThrow(UserNotFoundException::new);
		if (!parties.getPartyPost().getStatus().equals(Status.NO_SHOW_REPORTING)) {
			throw new BadRequestException("노쇼 신고 기간이 만료되었습니다");
		}
		List<User> users = parties.getUsers();
		for (User usersIf : users) {
			if (!usersIf.equals(user)) {
				throw new BadRequestException("파티 구성원이 아닙니다");
			}
		}
		if (noShowRepository.existsByReporterIdAndPostIdAndReportedId(usersDetails.getId(),
			parties.getPartyPost().getId(), user.getId())) {
			throw new BadRequestException("이미 신고한 유저입니다");
		}

		NoShow noShow = new NoShow(usersDetails, parties.getPartyPost(), user);
		noShow.PlusNoShowReportCnt();
		noShowRepository.save(noShow);
		return ApiResponse.ok("노쇼 신고 완료");
	}

	//status.END 상태 파티글에 대한 노쇼 신고 처리
	@Transactional
	public void checkingNoShow(List<PartyPost> posts) {
		for (PartyPost partyPost : posts ) {
			partyPost.ChangeStatusEnd();
			Parties endParties = partyRepository.findByPartyPostId(partyPost.getId())
					.orElseThrow(IllegalArgumentException::new);
			// 파티 유저 size 를 알기 위해
			List<User> users = endParties.getUsers();
			List<NoShow> noShowList = noShowRepository.findAllByPostId(partyPost.getId());
			for (NoShow noShowIf : noShowList) {
				if (noShowIf.getNoShowReportCnt() >= Math.round(users.size() / 2)) {
					noShowIf.getReported().getProfiles().plusNoShowCnt();
				}
			}
		}
//		return ApiResponse.ok("노쇼 처리 완료");
	}

	//private
	private User findByUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);
	}

	private void isMySelf(User users, Long blockedId) {
		if (users.getId().equals(blockedId))
			throw new BadRequestException("본인 아이디입니다");
	}
}

