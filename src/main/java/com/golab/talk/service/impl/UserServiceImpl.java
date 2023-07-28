package com.golab.talk.service.impl;

import com.golab.talk.domain.User;
import com.golab.talk.dto.UserDto;
import com.golab.talk.repository.UserRepository;
import com.golab.talk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public void registerUser(UserDto userDto) {
        User user = userDto.toEntity();
        userRepository.save(user);
    }

    @Override
    public String duplicationCheckNickname(String nickname) {
        String oldNickname;
        oldNickname = userRepository.checkNickname(nickname);
        return oldNickname;
    }

    @Override
    public String duplicationCheckId(String userId) {
        String oldUserId;
        oldUserId = userRepository.checkUserId(userId);
        return oldUserId;
    }

    @Override
    public String duplicationCheckEmail(String email) {
        String oldEmail;
        oldEmail = userRepository.checkEmail(email);
        return oldEmail;
    }

    @Override
    public String getUserId(String email) {
        String userId;
        userId = userRepository.findUserIdByEmail(email);
        return userId;
    }

    @Override
    public String getPassword(UserDto userDto) {
        String password;
        password = userRepository.findPasswordByEmailAndId(userDto.getEmail(),userDto.getUserId());
        return password;
    }

    @Override
    public UserDto login(UserDto userDto) {
        System.out.println("abc\n");
        UserDto result = userRepository.login(userDto.getUserId(), userDto.getPassword());
        System.out.println("\n");
        return result;
    }
}
