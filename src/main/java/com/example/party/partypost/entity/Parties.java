package com.example.party.partypost.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.example.party.user.entity.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "party")
public class Parties {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	@JoinColumn(name = "partyPost_id")
	private PartyPost partyPost;
	@OneToMany
	private List<User> users;

	public Parties(PartyPost partyPost) {
		this.users = new ArrayList<>();
		this.partyPost = partyPost;
	}

	public List<User> getUsers() {
		return users;
	}

	public void addUsers(User user) {
		this.users.add(user);
	}

	// public void removeUsers(User user) { this.users.remove(user); }
}
