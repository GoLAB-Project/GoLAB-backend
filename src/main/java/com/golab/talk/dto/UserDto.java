package com.golab.talk.dto;

import com.golab.talk.domain.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {
    private String userId;
    private String nickname;
    private String email;
    private String password;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .nickname(nickname)
                .email(email)
                .password(password)
                .build();
    }

    public UserDto(String userId, String email){
        this.userId = userId;
        this.email = email;
    }

    public void setUserDtoLogin(UserDto userDto){
        String password = userDto.getEmail();
        userDto.setEmail("");
        userDto.setPassword(password);
    }
}
