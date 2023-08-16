package com.golab.talk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.golab.talk.dto.LoginDto;
import com.golab.talk.dto.UserDto;
import com.golab.talk.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("user")
@SessionAttributes("loggedInUser") // "loggedInUser" 모델 속성을 세션에 저장하도록 지정
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/join")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        try {
            userService.registerUser(userDto);
            return new ResponseEntity<>("회원가입 성공했어요.", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>("회원가입을 실패했어요." + userDto.toEntity().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/name={name}")
    public ResponseEntity<String> duplicationCheckName(@PathVariable("name") String name) {
        String oldName = userService.duplicationCheckName(name);

        if (oldName == null) {
            return new ResponseEntity<>("사용가능한 닉네임입니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다른 닉네임을 사용해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/userId={userId}")
    public ResponseEntity<String> duplicationCheckUserId(@PathVariable("userId") String userId) {
        String oldUserId = userService.duplicationCheckId(userId);

        if (oldUserId == null) {
            return new ResponseEntity<>("사용가능한 아이디입니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다른 아이디를 사용해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/email={email}")
    public ResponseEntity<String> duplicationCheckEmail(@PathVariable("email") String email) {
        String oldEmail = userService.duplicationCheckEmail(email);

        if (oldEmail == null) {
            return new ResponseEntity<>("사용가능한 이메일입니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다른 이메일을 사용해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findId/email={email:.+}")
    public ResponseEntity<String> getUserId(@PathVariable("email") String email) {
        String userId = userService.getUserId(email);

        if (userId != null) {
            return new ResponseEntity<>("회원님의 아이디는 " + userId + " 입니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 이메일로 가입된 아이디가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/findPassword")
    public ResponseEntity<String> getPassword(@RequestBody UserDto userDto) {
        String password = userService.getPassword(userDto);

        if (password != null) {
            return new ResponseEntity<>("회원님의 비밀번호는 " + password + " 입니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("비밀번호를 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        UserDto loggedInUser = userService.login(loginDto);

        if (loggedInUser != null) {
            // 로그인에 성공한 경우 "loggedInUser" 모델 속성에 유저 정보를 저장하고 세션에 저장
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", loggedInUser);
            UserDto a = (UserDto)session.getAttribute("loggedInUser");
            return new ResponseEntity<>("로그인에 성공했습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("로그인에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        //세션을 삭제
        HttpSession session = request.getSession(false);
        // session이 null이 아니라는건 기존에 세션이 존재했었다는 뜻이므로
        // 세션이 null이 아니라면 session.invalidate()로 세션 삭제해주기.
        if(session != null) {
            session.invalidate();
            return new ResponseEntity<>("로그아웃 되었습니다.", HttpStatus.OK);
        }
        else return new ResponseEntity<>("로그인 되지 않았습니다.", HttpStatus.OK);
    }

    @GetMapping("/password={password}")
    public ResponseEntity<String> checkPassword(@PathVariable("password") String password, HttpServletRequest request) {
        if(!existSession(request)) return new ResponseEntity<>("세션이 만료되었습니다.\n로그인을 다시 해주세요.", HttpStatus.OK);
        boolean checking = userService.checkPassword(password, request);
        if (checking) {
            return new ResponseEntity<>("비밀번호가 일치합니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("비밀번호가 일치하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/update/password/{password}")
    public ResponseEntity<String> updatePassword(@PathVariable("password") String password, HttpServletRequest request) {
        if(!existSession(request)) return new ResponseEntity<>("세션이 만료되었습니다.\n로그인을 다시 해주세요.", HttpStatus.OK);
        // 비밀번호 업데이트 로직 실행
        int checking = userService.updatePassword(password, request);
        if(checking==1) {
            return new ResponseEntity<>("비밀번호 변경 완료.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("비밀번호 변경 실패.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/update/name/{name}")
    public ResponseEntity<String> updateName(@PathVariable("name") String name, HttpServletRequest request) {
        if(!existSession(request)) return new ResponseEntity<>("세션이 만료되었습니다.\n로그인을 다시 해주세요.", HttpStatus.OK);
        // 비밀번호 업데이트 로직 실행
        int checking = userService.updateName(name, request);
        if(checking==1) {
            return new ResponseEntity<>("닉네임 변경 완료.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("닉네임 변경 실패.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean existSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null) return false;
        else return true;
    }

    // 다른 요청에서 "loggedInUser" 모델 속성을 사용할 수 있도록 @ModelAttribute로 해당 값을 가져옴
    //    @ModelAttribute("loggedInUser")
    //    public UserDto loggedInUser() {
    //        // 아래는 세션에 "loggedInUser" 속성이 없을 때 기본으로 사용할 값이나, 실제로는 로그인 성공 시 세션에 저장된 값을 반환해야 함
    //        return new UserDto();
    //    }

}