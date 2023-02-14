package com.example.party.global;

import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseEntity {
	protected LocalDateTime createdAt;
	protected LocalDateTime modifiedAt;
}
