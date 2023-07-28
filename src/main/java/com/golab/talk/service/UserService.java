package com.golab.talk.service;

import com.golab.talk.domain.User;
import com.golab.talk.dto.UserDto;

public interface UserService {

    void registerUser(UserDto userDto);
    String duplicationCheckNickname(String nickname);

    String duplicationCheckId(String userId);

    String duplicationCheckEmail(String email);

    String getUserId(String email);

    String getPassword(UserDto userDto);

    UserDto login(UserDto userDto);
}
