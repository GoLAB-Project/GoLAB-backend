package com.golab.talk.controller;

import com.golab.talk.service.SocialLoginService;
import com.golab.talk.service.util.ApiUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;


@RestController
@RequestMapping("/login/social")
public class SocialLoginController {

    @Autowired
    SocialLoginService socialLoginService;

    ApiUtil apiUtil = new ApiUtil();

    @GetMapping("/google")
    public void googleLogin(HttpServletResponse response) throws IOException{
        response.sendRedirect(socialLoginService.connectGoogleLogin());
    }

    @GetMapping("/google/callback")
    public ResponseEntity<HashMap<String, String>> successGoogleLogin(@RequestParam("code") String code){
        String accessTokenUrl = socialLoginService.getGoogleAccessTokenUrl(code);
        String accessToken = apiUtil.getAccessToken(accessTokenUrl);
        HashMap<String, String> userInfo = socialLoginService.getGoogleLoginInfo(accessToken);

        return new ResponseEntity<>(userInfo,HttpStatus.OK);
    }

    @GetMapping("/naver")
    public void naverLogin(HttpServletResponse response) throws IOException{
        response.sendRedirect(socialLoginService.connectNaverLogin());
    }


    @GetMapping("/naver/callback")
    public ResponseEntity<HashMap<String, String>> successNaverLogin(@RequestParam("code") String code) {
        String accessTokenUrl = socialLoginService.getNaverAccessTokenUrl(code);
        String accessToken = apiUtil.getAccessToken(accessTokenUrl);
        HashMap<String, String> userInfo = socialLoginService.getNaverLoginInfo(accessToken);

        return new ResponseEntity<>(userInfo,HttpStatus.OK);
    }

    @GetMapping("/kakao")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect(socialLoginService.connectKakaoLogin());
    }


    @GetMapping("/kakao/callback")
    public ResponseEntity<HashMap<String, String>> getKakaoInfo(@RequestParam("code") String code) {
        String accessTokenUrl = socialLoginService.getKakaoAccessTokenUrl(code);
        String accessToken = apiUtil.getAccessToken(accessTokenUrl);
        HashMap<String, String> userInfo = socialLoginService.getKakaoLoginInfo(accessToken);

        return new ResponseEntity<>(userInfo,HttpStatus.OK);
    }




}
