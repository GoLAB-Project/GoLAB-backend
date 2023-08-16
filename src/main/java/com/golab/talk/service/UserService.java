package com.golab.talk.service;

import com.golab.talk.dto.LoginDto;
import com.golab.talk.dto.UserDto;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    void registerUser(UserDto userDto);

    String duplicationCheckName(String name);

    String duplicationCheckId(String userId);

    String duplicationCheckEmail(String email);

    String getUserId(String email);

    String getPassword(UserDto userDto);

    UserDto login(LoginDto loginDto);

    boolean checkPassword(String password, HttpServletRequest request);

    int updatePassword(String password, HttpServletRequest request);

    int updateName(String name, HttpServletRequest request);
}