package com.golab.talk.service;

import java.util.HashMap;

public interface SocialLoginService {

    String getGoogleAccessTokenUrl(String accessCode);

    String connectGoogleLogin();

    HashMap<String, String> getGoogleLoginInfo(String accessToken);

    String connectNaverLogin();
    String getNaverAccessTokenUrl(String code);

    HashMap<String, String> getNaverLoginInfo(String accessToken);

    String connectKakaoLogin();
    String getKakaoAccessTokenUrl(String code);
    HashMap<String, String> getKakaoLoginInfo(String accessToken);

    HashMap<String, String> connectSocialAccount(int platformType, String email);



}
