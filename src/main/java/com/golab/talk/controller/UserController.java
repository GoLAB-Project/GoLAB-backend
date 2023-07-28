package com.golab.talk.controller;

import com.golab.talk.domain.User;
import com.golab.talk.dto.UserDto;
import com.golab.talk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@SessionAttributes("loggedInUser") // "loggedInUser" 모델 속성을 세션에 저장하도록 지정
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/join")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto){
        try {
            userService.registerUser(userDto);
            return new ResponseEntity<>("회원가입 성공했어요.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("회원가입을 실패했어요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/nickname={nickname}")
    public ResponseEntity<String> duplicationCheckNickname(@PathVariable("nickname") String nickname){
        String oldNickname = userService.duplicationCheckNickname(nickname);

        if (oldNickname == null) {
            return new ResponseEntity<>("사용가능한 닉네임입니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다른 닉네임을 사용해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/userId={userId}")
    public ResponseEntity<String> duplicationCheckUserId(@PathVariable("userId") String userId){
        String oldUserId = userService.duplicationCheckId(userId);

        if (oldUserId == null) {
            return new ResponseEntity<>("사용가능한 아이디입니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다른 아이디를 사용해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/email={email}")
    public ResponseEntity<String> duplicationCheckEmail(@PathVariable("email") String email){
        String oldEmail = userService.duplicationCheckEmail(email);

        if (oldEmail == null) {
            return new ResponseEntity<>("사용가능한 이메일입니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다른 이메일을 사용해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findId/email={email:.+}")
    public ResponseEntity<String> getUserId(@PathVariable("email") String email){
        String userId = userService.getUserId(email);

        if (userId != null) {
            return new ResponseEntity<>("회원님의 아이디는 "+userId+" 입니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 이메일로 가입된 아이디가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/findPassword")
    public ResponseEntity<String> getPassword(@RequestBody UserDto userDto){
        String password = userService.getPassword(userDto);

        if (password != null) {
            return new ResponseEntity<>("회원님의 비밀번호는 "+password+" 입니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("비밀번호를 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto, Model model){
        // 로그인 처리를 위해 UserService의 login 메서드를 호출
//        userDto.setUserDtoLogin(userDto);
        System.out.println(userDto.getUserId());
        System.out.println(userDto.getPassword());
        UserDto loggedInUser = userService.login(userDto);

        if (loggedInUser != null) {
            // 로그인에 성공한 경우 "loggedInUser" 모델 속성에 유저 정보를 저장하고 세션에 저장
            model.addAttribute("loggedInUser", loggedInUser);

            return new ResponseEntity<>("로그인에 성공했습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("로그인에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 다른 요청에서 "loggedInUser" 모델 속성을 사용할 수 있도록 @ModelAttribute로 해당 값을 가져옴
//    @ModelAttribute("loggedInUser")
//    public UserDto loggedInUser() {
//        // 아래는 세션에 "loggedInUser" 속성이 없을 때 기본으로 사용할 값이나, 실제로는 로그인 성공 시 세션에 저장된 값을 반환해야 함
//        return new UserDto();
//    }

}