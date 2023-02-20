package com.example.party.restrictions.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class NoShow {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

}
