package com.example.party.restrictions.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.example.party.restrictions.entity.Blocks;
import com.example.party.restrictions.entity.NoShow;
import com.example.party.restrictions.entity.PostReport;
import com.example.party.restrictions.entity.UserReport;
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
		for (Blocks blockIf : user.getBlockedList()) {
			if (Objects.equals(blockIf.getBlocked().getId(), (blocked.getId()))) {
				throw new IllegalArgumentException("이미 신고한 유저입니다");
			}
		}
		// List<Blocks> blocks = blockRepository.findAllByBlockerId(user.getId());
		// for (Blocks block : blocks) {
		// 	if (Objects.equals(block.getBlockedId(), blocked.getId())) {
		// 		throw new IllegalArgumentException("이미 신고한 유저입니다");
		// 	}
		// }
		Blocks block = new Blocks(user, blocked);
		block.addBlocks(blocked);
		blockRepository.save(block);
		return ItemApiResponse.ok("차단등록 완료", new BlockResponse(block));
	}

	//차단해제
	public ApiResponse unBlockUser(Long userId, User user) {
		User blocked = findByUser(userId);
		for (Blocks blockIf : user.getBlockedList()) {
			if (Objects.equals(blockIf.getBlocked().getId(), (blocked.getId()))) {
				throw new IllegalArgumentException("이미 신고한 유저입니다");
			}
		}
		// List<Blocks> blocks = blockRepository.findAllByBlockerId(user.getId());
		// for (Blocks block : blocks) {
		// 	if (!Objects.equals(block.getBlockedId(), blocked.getId())) {
		// 		throw new IllegalArgumentException("신고 목록에 유저가 없습니다");
		// 	}
		// 	blockRepository.delete(block);
		// }
		Blocks blocker = blockRepository.findByBlockerId(user.getId())
			.orElseThrow(UserNotFoundException::new);
		blocker.removeBlocks(blocked);
		blockRepository.delete(blocker);
		return ApiResponse.ok("차단해제 완료");
	}

	//차단목록 조회
	public DataApiResponse<BlockResponse> blocks(int page, User user) {
		Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
		List<Blocks> blocks = blockRepository.findAllByBlockerId(user.getId(), pageable);
		List<BlockResponse> blockResponse = blocks.stream().map(BlockResponse::new).collect(Collectors.toList());
		return DataApiResponse.ok("조회 성공", blockResponse);
	}

	//유저 신고
	public ItemApiResponse<ReportResponse> reportUsers(User user, ReportUserRequest request) {
		//신고할 유저
		User reportUser = findByUser(request.getUserId());
		if (reportUserRepository.existsByReporterIdAndReportedId(user.getId(), reportUser.getId())) {
			throw new BadRequestException("이미 신고한 유저입니다");
		}
		UserReport userReports = new UserReport(user, reportUser, request);
		reportUserRepository.save(userReports);
		return ItemApiResponse.ok("신고 완료", new ReportResponse(userReports));
	}

	//게시글 신고
	public ItemApiResponse<ReportPostResponse> reportPosts(User user, ReportPostRequest request) {
		PartyPost post = partyPostRepository.findById(request.getPostId())
			.orElseThrow(() -> new IllegalArgumentException("없는 파티 모집글 입니다"));
		Optional<PostReport> checkPostReport = reportPostRepository
			.findByUserIdAndReportPostId(user.getId(), post.getId());
		if (checkPostReport.isPresent()) {
			throw new BadRequestException("이미 신고한 게시글입니다");
		}
		PostReport postReport = new PostReport(user, request, post);
		reportPostRepository.save(postReport);
		return ItemApiResponse.ok("게시글 신고 완료", new ReportPostResponse(postReport));
	}

	//노쇼 신고
	public ApiResponse reportNoShow(User userDetails, Long noShowUserId) {
		//신고할 유저
		User user = findByUser(noShowUserId);
		//로그인한 유저의 파티객체
		Party party = partyRepository.findById(userDetails.getId())
			.orElseThrow(UserNotFoundException::new);
		if (!party.getPartyPost().getStatus().equals(Status.NO_SHOW_REPORTING)) {
			throw new BadRequestException("노쇼 신고 기간이 만료되었습니다");
		}
		List<User> users = party.getUsers();
		for (User userIf : users) {
			if (!userIf.equals(user)) {
				throw new BadRequestException("파티 구성원이 아닙니다");
			}
		}
		if (noShowRepository.existsByReporterIdAndPostIdAndReportedId(userDetails.getId(),
			party.getPartyPost().getId(), user.getId())) {
			throw new BadRequestException("이미 신고한 유저입니다");
		}
		NoShow noShow = new NoShow(userDetails, party.getPartyPost(), user);
		noShow.PlusNoShowReportCnt();
		noShowRepository.save(noShow);
		return ApiResponse.ok("노쇼 신고 완료");
	}

	//status.END 상태 파티글에 대한 노쇼 신고 처리
	public ApiResponse checkingNoShow(PartyPost post) {
		Party endParty = partyRepository.findByPartyPostId(post.getId())
			.orElseThrow(IllegalArgumentException::new);
		List<User> users = endParty.getUsers();
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

