package com.example.party.partypost.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.party.partypost.entity.PartyPost;

public interface PartyPostRepository extends JpaRepository<PartyPost, Long> {

	//모집글 전체 조회
	Page<PartyPost> findAllByActiveIsTrue(Pageable pageable);

	// postId 로 특정 모집글 가져오기
	Optional<PartyPost> findById(Long postId);

	//내가 작성한 모집글 리스트 조회
	List<PartyPost> findByUserId(Long userId, Pageable pageable);

	//현재 active:true, status:'FINDING' 상태고 close_date 가 [현재] 이전인 게시글을 FOUND 로 바꿈
	@Modifying
	@Query(value = "UPDATE  partyPost p SET p.status = 'FOUND' WHERE  p.active = true AND p.status = 'FINDING'  AND p.closeDate <= CURRENT_TIMESTAMP")
	void changeStatusToFoundWhenCloseDate();


	//제목 혹은 주소 값이 검색문자에 포함시에 모집글 리스트 조회
	List<PartyPost> findByTitleContainingOrAddressContaining(String title, String address, Pageable pageable);

}
