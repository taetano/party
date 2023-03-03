package com.example.party.partypost.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.example.party.user.entity.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Party {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne()
	private PartyPost partyPost;

	//브릿지 테이블
	@OneToMany()
	@JoinColumn(name = "party_id")
	private List<User> users = new ArrayList<>();

	public Party(PartyPost partyPost) {
		this.partyPost = partyPost;
		autoAddUser(partyPost.getUser());
	}

	public List<User> getUsers() {
		return users;
	}

	public void autoAddUser(User user) {
		this.users.add(user);
	}

	public void addUser(User user) {
		this.users.add(user);
	}

	// public void removeUsers(User user) { this.users.remove(user); }
}
