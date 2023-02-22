package com.example.party.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.party.chat.entity.MsgRoom;

public interface MsgRoomRepository extends JpaRepository<MsgRoom, Long> {
	@Query(value = "select distinct m from MsgRoom m where m.user.id=:userId or m.other.id=:userId")
	List<MsgRoom> findAllByUserId(@Param("userId") long userId);

}
