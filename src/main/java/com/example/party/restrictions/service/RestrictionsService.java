package com.example.party.restrictions.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.party.user.exception.global.common.ApiResponse;
import com.example.party.user.exception.global.common.DataApiResponse;
import com.example.party.user.exception.global.common.ItemApiResponse;
import com.example.party.user.exception.global.exception.BadRequestException;
import com.example.party.partypost.entity.Partys;
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
	private final PartyRepository partyRepository;
	private final BlockRepository blockRepository;
	private final NoShowRepository noShowRepository;
	private final ReportUserRepository reportUserRepository;
	private final ReportPostRepository reportPostRepository;
	private final PartyPostRepository partyPostRepository;

	//차단등록
	public ItemApiResponse<BlockResponse> blockUser(Long userId, User user) {
		User blocked = findByUser(userId);
		List<Blocks> blocks = blockRepository.findAllByBlockerId(user.getId());
		if (blocks.size() > 0) {
			for (Blocks blockIf : blocks) {
				if (Objects.equals(blockIf.getBlocked().getId(), (blocked.getId()))) {
					throw new CheckedBlocksException("이미 신고한 유저입니다");
				}
			}
		}
		Blocks block = new Blocks(user, blocked);
		blockRepository.save(block);
		return ItemApiResponse.ok("차단등록 완료", new BlockResponse(block));
	}

	//차단해제
	public ApiResponse unBlockUser(Long userId, User user) {
		User blocked = findByUser(userId);
		List<Blocks> blocks = blockRepository.findAllByBlockerId(user.getId());
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
	public DataApiResponse<BlockResponse> getBlockedList(int page, User user) {
		Pageable pageable = PageRequest.of(page, 10);
		List<Blocks> blocks = blockRepository.findAllByBlockerId(user.getId(), pageable);
		List<BlockResponse> blockResponse = blocks.stream().map(BlockResponse::new).collect(Collectors.toList());
		return DataApiResponse.ok("조회 성공", blockResponse);
	}

	//유저 신고
	public ApiResponse reportUsers(User user, ReportUserRequest request) {
		//신고할 유저
		User reportUser = findByUser(request.getUserId());
		if (reportUserRepository.existsByReporterIdAndReportedId(user.getId(), reportUser.getId())) {
			throw new BadRequestException("이미 신고한 유저입니다");
		}
		UserReport userReports = new UserReport(user, reportUser, request);
		reportUserRepository.save(userReports);
		return ApiResponse.ok("유저 신고 완료");
	}

	//모집글 신고
	public ApiResponse reportPosts(User user, ReportPostRequest request) {
		PartyPost post = partyPostRepository.findById(request.getPostId())
			.orElseThrow(() -> new PartyPostNotFoundException());
		Optional<PostReport> checkPostReport = reportPostRepository
			.findByUserIdAndReportPostId(user.getId(), post.getId());
		if (checkPostReport.isPresent()) {
			throw new BadRequestException("이미 신고한 모집글입니다");
		}
		PostReport postReports = new PostReport(user, request, post);
		reportPostRepository.save(postReports);
		return ApiResponse.ok("모집글 신고 완료");
	}

	//노쇼 신고
	public ApiResponse reportNoShow(User userDetails, Long noShowUserId) {
		//신고할 유저
		User user = findByUser(noShowUserId);
		//로그인한 유저의 파티객체
		Partys partys = partyRepository.findById(userDetails.getId())
			.orElseThrow(UserNotFoundException::new);
		if (!partys.getPartyPost().getStatus().equals(Status.NO_SHOW_REPORTING)) {
			throw new BadRequestException("노쇼 신고 기간이 만료되었습니다");
		}
		List<User> users = partys.getUsers();
		for (User userIf : users) {
			if (!userIf.equals(user)) {
				throw new BadRequestException("파티 구성원이 아닙니다");
			}
		}
		if (noShowRepository.existsByReporterIdAndPostIdAndReportedId(userDetails.getId(),
			partys.getPartyPost().getId(), user.getId())) {
			throw new BadRequestException("이미 신고한 유저입니다");
		}

		NoShow noShow = new NoShow(userDetails, partys.getPartyPost(), user);
		noShow.PlusNoShowReportCnt();
		noShowRepository.save(noShow);
		return ApiResponse.ok("노쇼 신고 완료");
	}

	//status.END 상태 파티글에 대한 노쇼 신고 처리
	public ApiResponse checkingNoShow(PartyPost post) {
		Partys endPartys = partyRepository.findByPartyPostId(post.getId())
			.orElseThrow(IllegalArgumentException::new);
		List<User> users = endPartys.getUsers();
		List<NoShow> noShowList = noShowRepository.findAllByPostId(post.getId());
		for (NoShow noShowIf : noShowList) {
			if (noShowIf.getNoShowReportCnt() >= Math.round(users.size() / 2)) {
				noShowIf.getReported().getProfile().plusNoShowCnt();
			}
		}
		return ApiResponse.ok("노쇼 처리 완료");
	}

	//private
	private User findByUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);
	}
}

