package com.example.party.restrictions.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.global.exception.BedRequestException;
import com.example.party.restrictions.dto.BlockResponse;
import com.example.party.restrictions.dto.ReportNoShowRequest;
import com.example.party.restrictions.dto.ReportPostRequest;
import com.example.party.restrictions.dto.ReportResponse;
import com.example.party.restrictions.dto.ReportUserRequest;
import com.example.party.restrictions.entity.Block;
import com.example.party.restrictions.entity.Report;
import com.example.party.restrictions.exception.CheckedBlocksException;
import com.example.party.restrictions.repository.BlockRepository;
import com.example.party.restrictions.repository.ReportRepository;
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
	private final ReportRepository reportRepository;

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
	public ListResponseDto<?> blocks(User user) {
		List<Block> blocks = user.getBlocks();
		if (blocks.isEmpty()) {
			throw new CheckedBlocksException();
		}
		return ListResponseDto.ok("조회 성공", blocks);
	}

	//유저 신고
	public DataResponseDto reportUsers(User userDetails, ReportUserRequest request) {
		User user = findByUser(request.getUserId());
		List<Report> reports = userDetails.getReports();
		if (!reports.isEmpty()) {
			for (Report reportUser : reports) {
				if (Objects.equals(reportUser.getReportUserId(), user.getId())) {
					throw new BedRequestException("이미 신고한 유저입니다");
				}
			}
		}
		Report reportUser = new Report(userDetails, request, user);
		reportRepository.save(reportUser);
		userDetails.addReports(reportUser);
		return DataResponseDto.ok("신고 완료", new ReportResponse(reportUser));
	}

	//게시글 신고
	public ResponseDto reportPosts(User user, ReportPostRequest request) {
		return ResponseDto.ok("");
	}

	//노쇼 신고
	public ResponseDto reportNoShow(User user, ReportNoShowRequest request) {
		return ResponseDto.ok("");
	}

	//private
	private User findByUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);
	}

	// private Report findByReport(Long userId) {
	// 	return reportRepository.findById(userId)
	// 		.orElseThrow(UserNotFoundException::new);
	// }
}
