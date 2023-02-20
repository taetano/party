package com.example.party.restrictions.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.global.exception.BedRequestException;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.partypost.repository.PartyPostRepository;
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

	//차단등록
	public DataResponseDto<BlockResponse> blockUser(Long userId, User user) {
		User blocked = findByUser(userId);
		List<Block> blocks = user.getBlocks();
		if (!blocks.isEmpty()) {
			for (Block blockedUser : blocks) {
				if (Objects.equals(blockedUser.getBlockedId(), blocked.getId())) {
					throw new BedRequestException("이미 차단한 유저입니다");
				}
			}
		}
		Block block = new Block(user, blocked);
		blockRepository.save(block);
		user.addBlocks(block);
		return DataResponseDto.ok("차단등록 완료", new BlockResponse(block));
	}

	//차단해제
	public ResponseDto unBlockUser(Long userId, User user) {
		User blocked = findByUser(userId);
		List<Block> blocks = user.getBlocks();
		if (!blocks.isEmpty()) {
			for (Block blockedUser : blocks) {
				if (Objects.equals(blockedUser.getBlockedId(), blocked.getId())) {
					throw new BedRequestException("차단 목록에 없는 유저입니다");
				}
			}
		}
		Block block = blockRepository.findByBlockerAndBlocked(user, blocked)
			.orElseThrow(UserNotFoundException::new);
		blockRepository.delete(block);
		user.removeBlocks(block);
		return ResponseDto.ok("차단해제 완료");
	}

	//차단목록 조회
	public ListResponseDto<BlockResponse> blocks(User user) {
		List<Block> blocks = user.getBlocks();
		if (blocks.isEmpty()) {
			throw new CheckedBlocksException();
		}
		List<BlockResponse> collect = blocks.stream().map(BlockResponse::new).collect(Collectors.toList());

		return ListResponseDto.ok("조회 성공", collect);
	}

	//유저 신고
	public DataResponseDto<ReportResponse> reportUsers(User userDetails, ReportUserRequest request) {
		User user = findByUser(request.getUserId());
		List<UserReport> userReports = userDetails.getUserReports();
		if (!userReports.isEmpty()) {
			for (UserReport userReportUser : userReports) {
				if (Objects.equals(userReportUser.getReportUserId(), user.getId())) {
					throw new BedRequestException("이미 신고한 유저입니다");
				}
			}
		}
		UserReport userReportUser = new UserReport(userDetails, request, user);
		reportUserRepository.save(userReportUser);
		userDetails.addReports(userReportUser);
		return DataResponseDto.ok("신고 완료", new ReportResponse(userReportUser));
	}

	//게시글 신고
	public DataResponseDto<ReportPostResponse> reportPosts(User user, ReportPostRequest request) {
		PartyPost post = partyPostRepository.findById(request.getPostId())
			.orElseThrow(() -> new IllegalArgumentException(""));
		Optional<PostReport> checkPostReport = reportPostRepository.findByUserIdAndReportPostId(user.getId(),
			post.getId());
		if (checkPostReport.isPresent()) {
			throw new BedRequestException("이미 신고한 게시글입니다");
		}
		PostReport postReport = new PostReport(user.getId(), request, post);
		reportPostRepository.save(postReport);
		return DataResponseDto.ok("", new ReportPostResponse(postReport));
	}

	//노쇼 신고
	public ResponseDto reportNoShow(User userDetails, Long noShowUserId) {
		User user = findByUser(noShowUserId);

		return ResponseDto.ok("");
	}

	//private
	private User findByUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);
	}
}
