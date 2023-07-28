package com.golab.talk.dto;

import com.golab.talk.domain.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	private String userId;
	private String nickname;
	private String email;
	private String profileImage;

	public User toEntity() {
		return User.builder()
			.userId(userId)
			.nickname(nickname)
			.email(email)
			.profileImage(profileImage)
			.build();
	}

}
