package com.example.party.apply.service;

import com.example.party.global.dto.DataResponseDto;
import com.example.party.global.dto.ListResponseDto;

public interface IApplyService {
	DataResponseDto<?> cancelApply();
	ListResponseDto<?> findApplies();
	DataResponseDto<?> acceptApply();
	DataResponseDto<?> rejectApply();
}
