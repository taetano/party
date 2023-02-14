package com.example.party.partypost.repository;

import com.example.party.partypost.entity.PartyPost;
import com.example.party.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PartyPostRepository extends JpaRepository<PartyPost, Long> {

  //모집글 전체 조회
  List<PartyPost> findAllByOrderById();

  // postId 로 특정 모집글 가져오기
  Optional<PartyPost> findById(Long postId);

  //내가 작성한 모집글 리스트 조회
  List<PartyPost> findByUserId(User user, Pageable pageable);

}
