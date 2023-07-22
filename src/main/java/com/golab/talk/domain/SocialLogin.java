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
@Table(name = "SOCIAL_LOGIN")
@Getter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SocialLogin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SOCIAL_ID", nullable = false)
	private int socialId;

	@Column(name = "USER_ID", nullable = false)
	private String userId;
	@Column(name = "PLATFORM_TYPE", nullable = false)
	private int platformType;
	@Column(name = "SOCIAL_EMAIL", nullable = false)
	private String socialEmail;
	@Column(name = "TOKEN", nullable = false)
	private String token;

}
