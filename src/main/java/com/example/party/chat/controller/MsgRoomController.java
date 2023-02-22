package com.example.party.chat.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.party.chat.dto.CreateRoomRequest;
import com.example.party.chat.dto.MsgRoomResponse;
import com.example.party.chat.service.MsgService;
import com.example.party.user.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MsgRoomController {
	private final MsgService msgService;

	@GetMapping("/chatting")
	public String chatting(Model model, @AuthenticationPrincipal User user) {
		model.addAttribute("user", user);
		return "chat";
	}

	@PostMapping("/rooms")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public void createRoom(@RequestBody CreateRoomRequest request, @AuthenticationPrincipal User user) {
		msgService.createRoom(request, user);
	}

	@GetMapping("/rooms")
	@ResponseBody
	public List<MsgRoomResponse> getAllRooms(@AuthenticationPrincipal User user) { // 유저 정보
		return msgService.findAllByUserId(user);
	}
}


