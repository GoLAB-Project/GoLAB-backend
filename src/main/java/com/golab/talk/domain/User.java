package com.golab.talk.domain;

import javax.persistence.*;

import com.golab.talk.dto.UserDto;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

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
@DynamicInsert
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private int id;

	@Column(name = "USER_ID", nullable = false, unique = true)
	private String userId;
	@Column(name = "NAME", nullable = false, unique = true)
	private String name;
	@Column(name = "EMAIL", nullable = false)
	private String email;
	@Column(name = "PASSWORD", nullable = false)
	private String password;
	@Column(name = "PROFILE_IMG_URL")
	private String profileImgUrl;
	@Column(name = "CREATED_AT")
	private String createdAt;
	@Column(name = "UPDATED_AT")
	private String updatedAt;

	public User(int id, String userId, String name, String email, String password){
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public UserDto toDto() {
		return UserDto.builder()
				.id(id)
				.userId(userId)
				.name(name)
				.email(email)
				.password(password)
				.build();
	}

}
