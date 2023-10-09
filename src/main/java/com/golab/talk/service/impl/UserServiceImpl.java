package com.golab.talk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golab.talk.domain.User;
import com.golab.talk.dto.LoginDto;
import com.golab.talk.dto.UserDto;
import com.golab.talk.repository.UserRepository;
import com.golab.talk.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void registerUser(UserDto userDto) {
        User user = userDto.toEntity();
        System.out.println("before save");
        userRepository.save(user);
        System.out.println("after save");
    }

    @Override
    public String duplicationCheckName(String name) {
        String oldName;
        oldName = userRepository.checkName(name);
        return oldName;
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
        password = userRepository.findPasswordByEmailAndId(userDto.getEmail(), userDto.getUserId());
        return password;
    }

    @Override
    public UserDto login(LoginDto loginDto) {
        User user = userRepository.findUserByUserIdAndPassword(loginDto.getUserId(), loginDto.getPassword());
        UserDto userDto = (user == null ? null : user.toDto());
        return userDto;
    }

    @Override
    public boolean checkPassword(String password, HttpServletRequest request) {
        String dbPassword = "";
        HttpSession session = request.getSession(false);
        if(session!=null) {
            String sessionUserId = ((UserDto) session.getAttribute("loggedInUser")).getUserId();
            dbPassword = userRepository.checkPassword(sessionUserId);
        }
        boolean result = (password.equals(dbPassword) ? true : false);
        return result;
    }

    @Override
    @Transactional
    public int updatePassword(String password, HttpServletRequest request) {
        int check = 0;
        HttpSession session = request.getSession(false);
        if(session!=null) {
            String sessionUserId = ((UserDto) session.getAttribute("loggedInUser")).getUserId();
            check = userRepository.updatePassword(sessionUserId, password);
        }
        return check;
    }

    @Override
    @Transactional
    public int updateName(String name, HttpServletRequest request) {
        int check = 0;
        HttpSession session = request.getSession(false);
        if(session!=null) {
            String sessionUserId = ((UserDto) session.getAttribute("loggedInUser")).getUserId();
            check = userRepository.updateName(sessionUserId, name);
        }
        return check;
    }
}