package com.example.party.restriction.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.example.party.partypost.entity.Parties;
import com.example.party.restriction.dto.NoShowRequest;
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
import com.example.party.restriction.dto.BlockResponse;
import com.example.party.restriction.dto.ReportPostRequest;
import com.example.party.restriction.dto.ReportUserRequest;
import com.example.party.restriction.entity.Blocks;
import com.example.party.restriction.entity.NoShow;
import com.example.party.restriction.entity.ReportPost;
import com.example.party.restriction.entity.ReportUser;
import com.example.party.global.exception.NotFoundException;
import com.example.party.restriction.repository.BlocksRepository;
import com.example.party.restriction.repository.NoShowRepository;
import com.example.party.restriction.repository.ReportPostRepository;
import com.example.party.restriction.repository.ReportUserRepository;
import com.example.party.user.exception.UserNotFoundException;
import com.example.party.user.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class RestrictionService {

	private final UserRepository userRepository;
	private final PartyRepository partyRepository;
	private final BlocksRepository blocksRepository;
	private final NoShowRepository noShowRepository;
	private final ReportUserRepository reportUserRepository;
	private final ReportPostRepository reportPostRepository;
	private final PartyPostRepository partyPostRepository;

	//차단등록
	public ItemApiResponse<BlockResponse> blockUser(User user, Long userId) {
		User blocked = findByUser(userId);
		isMySelf(user, userId);
		List<Blocks> blocks = getBlocks(user.getId());
		if (blocks.size() > 0) {
			for (Blocks blockIf : blocks) {
				if (Objects.equals(blockIf.getBlocked().getId(), (blocked.getId()))) {
					throw new BadRequestException("이미 신고한 유저입니다");
				}
			}
		}
		Blocks block = new Blocks(user, blocked);
		blocksRepository.save(block);
		return ItemApiResponse.ok("차단등록 완료", new BlockResponse(block));
	}

	//차단해제
	public ApiResponse unBlockUser(User user, Long userId) {
		User blocked = findByUser(userId);
		isMySelf(user, userId);
		List<Blocks> blocks = getBlocks(user.getId());
		if (!blocks.isEmpty()) {
			for (Blocks block : blocks) {
				if (Objects.equals(block.getBlocked().getId(), (blocked.getId()))) {
					blocksRepository.delete(block);
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
		List<Blocks> blocks = blocksRepository.findAllByBlockerId(user.getId(), pageable);
		if (blocks.size() == 0) {
			throw new BadRequestException("차단한 유저가 없습니다");
		}
		List<BlockResponse> blockResponse = blocks.stream().map(BlockResponse::new).collect(Collectors.toList());
		return DataApiResponse.ok("조회 성공", blockResponse);
	}

	//유저 신고
	public ApiResponse createReportUser(User user, ReportUserRequest request) {
		//신고할 유저
		isMySelf(user, request.getUserId());
		User reported = findByUser(request.getUserId());
		if (reportUserRepository.existsByReporterIdAndReportedId(user.getId(), reported.getId())) {
			throw new BadRequestException("이미 신고한 유저입니다");
		}
		ReportUser reportUser = new ReportUser(user, reported, request);
		reportUserRepository.save(reportUser);
		return ApiResponse.ok("유저 신고 완료");
	}

	//모집글 신고
	public ApiResponse createReportPost(User user, ReportPostRequest request) {
		PartyPost partyPost = partyPostRepository.findByIdAndActiveIsTrue(request.getPostId())
			.orElseThrow(PartyPostNotFoundException::new);
		if (partyPost.getUser().equals(user)) {
			throw new BadRequestException("본인이 작성한 글입니다");
		}
		if (reportPostRepository.existsByUserIdAndReportPostId(user.getId(), partyPost.getId())) {
			throw new BadRequestException("이미 신고한 모집글입니다");
		}
		ReportPost reportsPost = new ReportPost(user, request, partyPost);
		reportPostRepository.save(reportsPost);
		return ApiResponse.ok("모집글 신고 완료");
	}

	//노쇼 신고
	public ApiResponse reportNoShow(User user, NoShowRequest request) {
		int checkCnt = 0;
		isMySelf(user, request.getUserId());
		//신고할 유저
		User reported = findByUser(request.getUserId());
		Parties parties = getParties(request.getPostId());
		if (!parties.getPartyPost().getStatus().equals(Status.NO_SHOW_REPORTING)) {
			throw new BadRequestException("노쇼 신고 기간이 만료되었습니다");
		}
		List<User> users = parties.getUsers();
		if (!users.contains(reported)) {
			throw new BadRequestException("참여한 파티원이 아닙니다");
		}
		if (!users.contains(user)) {
			throw new BadRequestException("파티 구성원이 아닙니다");
		}
		if (noShowRepository.existsByReporterIdAndReportedIdAndPartyPostId(user.getId(), reported.getId(),
				parties.getPartyPost().getId())) {
			throw new BadRequestException("이미 신고한 유저입니다");
		}
		NoShow noShow = new NoShow(user, reported, parties.getPartyPost());
		noShow.PlusNoShowReportCnt();
		noShowRepository.save(noShow);
		return ApiResponse.ok("노쇼 신고 완료");
	}

	//status.END 상태 파티글에 대한 노쇼 신고 처리
	@Transactional
	public void checkingNoShow(List<PartyPost> posts) {
		for (PartyPost partyPost : posts ) {
			partyPost.ChangeStatusEnd();

			// 파티 유저 size 를 알기 위해
			Parties parties = getParties(partyPost.getId());
			List<User> users = parties.getUsers();

			List<NoShow> noShowList = noShowRepository.findAllByPartyPostId(partyPost.getId());
			for (NoShow noShowIf : noShowList) {
				if (noShowIf.getNoShowReportCnt() >= Math.round(users.size()/2)) {
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

	private List<Blocks> getBlocks(Long userId) {
		return blocksRepository.findAllByBlockerId(userId);
	}

	private Parties getParties(Long postId) {
		return partyRepository.findByPartyPostId(postId)
				.orElseThrow(NotFoundException::new);
	}
}

