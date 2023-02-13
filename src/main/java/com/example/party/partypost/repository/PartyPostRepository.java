package com.example.party.partypost.repository;

import com.example.party.partypost.entity.PartyPost;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyPostRepository extends JpaRepository<PartyPost, Long> {
  //모집글 전체 조회
  List<PartyPost> findAllByOrderById();

  // postId 로 특정 모집글 가져오기
  Optional<PartyPost> findById(Long postId);
}
