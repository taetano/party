package com.example.party.service;

import com.example.party.dto.request.AcceptApplicationCommand;
import com.example.party.dto.response.ApplicationResponse;
import com.example.party.dto.request.CancelApplicationCommand;
import com.example.party.dto.request.CreateApplicationCommand;
import com.example.party.dto.request.GetApplicationCommand;
import com.example.party.dto.request.RejectApplicationCommand;
import com.example.party.common.ApiResponse;
import com.example.party.common.DataApiResponse;

public interface IApplicationService {

	//모집 참가 신청
	ApiResponse createApplication(CreateApplicationCommand createApplicationCommand);

	//모집 참가 신청취소
	ApiResponse cancelApplication(CancelApplicationCommand cancelApplicationCommand);

	//참가신청자 리스트 조회(파티장 미포함. 내정보-나의모집글목록 상에서 사용)
	DataApiResponse<ApplicationResponse> getApplications(GetApplicationCommand getApplicationCommand);

	//모집 참가 수락
	ApiResponse acceptApplication(AcceptApplicationCommand acceptApplicationCommand);

	//모집 참가 거부
	ApiResponse rejectApplication(RejectApplicationCommand rejectApplicationCommand);

}
