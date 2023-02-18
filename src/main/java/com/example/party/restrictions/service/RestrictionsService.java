package com.example.party.restrictions.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;
import com.example.party.global.dto.ResponseDto;
import com.example.party.restrictions.dto.BlockResponse;
import com.example.party.restrictions.entity.Block;
import com.example.party.restrictions.exception.CheckedBlocksException;
import com.example.party.restrictions.repository.BlockRepository;
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

	//차단등록
	public DataResponseDto<BlockResponse> blockUser(Long userId, User user) {
		User blocked = findByUser(userId);
		Block block = new Block(user, blocked);
		blockRepository.save(block);
		user.addBlocks(block);
		BlockResponse blockResponse = new BlockResponse(block);
		return DataResponseDto.ok("차단등록 완료", blockResponse);
	}

	//차단해제
	public ResponseDto unBlockUser(Long userId, User user) {
		User blocked = findByUser(userId);
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
		return ListResponseDto.ok("조회 성공", user.getBlocks());
	}

	//private
	private User findByUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);
	}
}
