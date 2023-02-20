package com.example.party.restrictions.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.party.global.common.ApiResponse;
import com.example.party.global.common.DataApiResponse;
import com.example.party.global.common.ItemApiResponse;
import com.example.party.global.exception.BadRequestException;
import com.example.party.partypost.entity.Party;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.repository.PartyPostRepository;
import com.example.party.partypost.repository.PartyRepository;
import com.example.party.partypost.type.Status;
import com.example.party.restrictions.dto.BlockResponse;
import com.example.party.restrictions.dto.ReportPostRequest;
import com.example.party.restrictions.dto.ReportPostResponse;
import com.example.party.restrictions.dto.ReportResponse;
import com.example.party.restrictions.dto.ReportUserRequest;
import com.example.party.restrictions.entity.Block;
import com.example.party.restrictions.entity.PostReport;
import com.example.party.restrictions.entity.UserReport;
import com.example.party.restrictions.exception.CheckedBlocksException;
import com.example.party.restrictions.repository.BlockRepository;
import com.example.party.restrictions.repository.ReportPostRepository;
import com.example.party.restrictions.repository.ReportUserRepository;
import com.example.party.user.entity.User;
import com.example.party.user.exception.UserNotFoundException;
import com.example.party.user.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class RestrictionsService {

	private final UserRepository userRepository;
	private final BlockRepository blockRepository;
	private final ReportUserRepository reportUserRepository;
	private final ReportPostRepository reportPostRepository;
	private final PartyPostRepository partyPostRepository;
	private final PartyRepository partyRepository;

	//차단등록
	public ItemApiResponse<BlockResponse> blockUser(Long userId, User user) {
		User blocked = findByUser(userId);
		List<Block> blocks = user.getBlocks();
		if (!blocks.isEmpty()) {
			for (Block blockedUser : blocks) {
				if (Objects.equals(blockedUser.getBlockedId(), blocked.getId())) {
					throw new BadRequestException("이미 차단한 유저입니다");
				}
			}
		}
		Block block = new Block(user, blocked);
		blockRepository.save(block);
		user.addBlocks(block);
		return ItemApiResponse.ok("차단등록 완료", new BlockResponse(block));
	}

	//차단해제
	public ApiResponse unBlockUser(Long userId, User user) {
		User blocked = findByUser(userId);
		List<Block> blocks = user.getBlocks();
		if (!blocks.isEmpty()) {
			for (Block blockedUser : blocks) {
				if (Objects.equals(blockedUser.getBlockedId(), blocked.getId())) {
					throw new BadRequestException("차단 목록에 없는 유저입니다");
				}
			}
		}
		Block block = blockRepository.findByBlockerAndBlocked(user, blocked)
			.orElseThrow(UserNotFoundException::new);
		blockRepository.delete(block);
		user.removeBlocks(block);
		return ApiResponse.ok("차단해제 완료");
	}

	//차단목록 조회
	public DataApiResponse<BlockResponse> blocks(User user) {
		List<Block> blocks = user.getBlocks();
		if (blocks.isEmpty()) {
			throw new CheckedBlocksException();
		}
		List<BlockResponse> collect = blocks.stream().map(BlockResponse::new).collect(Collectors.toList());

		return DataApiResponse.ok("조회 성공", collect);
	}

	//유저 신고
	public ItemApiResponse<ReportResponse> reportUsers(User userDetails, ReportUserRequest request) {
		User user = findByUser(request.getUserId());
		List<UserReport> userReports = userDetails.getUserReports();
		if (!userReports.isEmpty()) {
			for (UserReport userReportUser : userReports) {
				if (Objects.equals(userReportUser.getReportUserId(), user.getId())) {
					throw new BadRequestException("이미 신고한 유저입니다");
				}
			}
		}
		UserReport userReportUser = new UserReport(userDetails, request, user);
		reportUserRepository.save(userReportUser);
		userDetails.addReports(userReportUser);
		return ItemApiResponse.ok("신고 완료", new ReportResponse(userReportUser));
	}

	//게시글 신고
	public ItemApiResponse<ReportPostResponse> reportPosts(User user, ReportPostRequest request) {
		PartyPost post = partyPostRepository.findById(request.getPostId())
			.orElseThrow(() -> new IllegalArgumentException(""));
		Optional<PostReport> checkPostReport = reportPostRepository.findByUserIdAndReportPostId(user.getId(),
			post.getId());
		if (checkPostReport.isPresent()) {
			throw new BadRequestException("이미 신고한 게시글입니다");
		}
		PostReport postReport = new PostReport(user.getId(), request, post);
		reportPostRepository.save(postReport);
		return ItemApiResponse.ok("게시글 신고 완료", new ReportPostResponse(postReport));
	}

	//노쇼 신고
	public ApiResponse reportNoShow(User userDetails, Long noShowUserId) {
		User user = findByUser(noShowUserId);
		Party party = partyRepository.findById(userDetails.getId())
			.orElseThrow(UserNotFoundException::new);
		if (!party.getPartyPost().getStatus().equals(Status.NO_SHOW_REPORTING)) {
			throw new BadRequestException("노쇼 신고 시간이 마감되었습니다");
		}
		List<User> users = party.getUsers();
		for (User userIf : users) {
			if (!userIf.equals(user)) {
				throw new BadRequestException("파티 구성원이 아닙니다");
			}
		}

		return ApiResponse.ok("노쇼 신고 완료");
	}

	//private
	private User findByUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);
	}
}
