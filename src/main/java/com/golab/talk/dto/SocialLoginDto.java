package com.golab.talk.dto;

import com.golab.talk.domain.SocialLogin;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialLoginDto {
    private String userId;
    private int platformType;
    private String socialEmail;

    public SocialLogin toEntity(String userId, int platformType, String socialEmail){
        return SocialLogin.builder()
                .userId(userId)
                .platformType(platformType)
                .socialEmail(socialEmail)
                .build();
    }

}
