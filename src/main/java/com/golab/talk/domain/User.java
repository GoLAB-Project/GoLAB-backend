package com.golab.talk.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "USER")
@Getter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@Column(name = "USER_ID", nullable = false)
	private String userId;

	@Column(name = "NICKNAME", nullable = false)
	private String nickname;
	@Column(name = "EMAIL", nullable = false)
	private String email;
	@Column(name = "PASSWORD", nullable = false)
	private String password;
}
