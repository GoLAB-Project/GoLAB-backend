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
    private int id;
    private String userId;
    private String name;
    private String email;
    private String password;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .name(name)
                .email(email)
                .password(password)
                .build();
    }
}