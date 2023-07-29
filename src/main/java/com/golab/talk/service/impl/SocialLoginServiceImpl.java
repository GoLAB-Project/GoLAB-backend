package com.golab.talk.service.impl;

import com.golab.talk.domain.SocialLogin;
import com.golab.talk.domain.User;
import com.golab.talk.repository.SocialLoginRepository;
import com.golab.talk.repository.UserRepository;
import com.golab.talk.service.SocialLoginService;
import com.golab.talk.service.util.ApiKey;
import com.golab.talk.service.util.ApiUtil;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SocialLoginServiceImpl implements SocialLoginService {

    public ApiUtil apiUtil = new ApiUtil();
    public ApiKey apiKey = new ApiKey();
    private final SocialLoginRepository socialLoginRepository;
    private final UserRepository userRepository;



    private final String GOOGLE_CLIENT_ID = apiKey.getGoogleClientId();
    private final String GOOGLE_SECRET = apiKey.getGoogleClientSecret();
    private final String GOOGLE_REDIRECT_URI = apiKey.getGoogleRedirectURI();
    private final String NAVER_CLIENT_ID = apiKey.getNaverClientId();
    private final String NAVER_REDIRECT_URI = apiKey.getNaverRedirectURI();
    private final String NAVER_SECRET = apiKey.getNaverClientSecret();
    private String naverState = apiUtil.generateRandomUserStr(11);
    private final String KAKAO_CLIENT_ID = apiKey.getKakaoClientId();
    private final String KAKAO_REDIRECT_URI = apiKey.getKakaoRedirectURI();



    //구글로그인
    public String connectGoogleLogin(){
        return "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + GOOGLE_CLIENT_ID + "&redirect_uri=" + GOOGLE_REDIRECT_URI
                + "&scope=email&response_type=code";
    }



    @Override
    public String getGoogleAccessTokenUrl(String code) {
        return "https://oauth2.googleapis.com/token?code=" + code + "&client_id=" + GOOGLE_CLIENT_ID + "&client_secret="
                + GOOGLE_SECRET + "&redirect_uri=" + GOOGLE_REDIRECT_URI + "&grant_type=authorization_code";
    }

    @Override
    public HashMap<String, String> getGoogleLoginInfo(String accessToken) {

        String header = "Bearer " + accessToken;
        String profileUrl = "https://www.googleapis.com/userinfo/v2/me";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);

        String responseBody = apiUtil.get(profileUrl, requestHeaders);
        JsonObject profileObject = apiUtil.gson.fromJson(responseBody, JsonObject.class);

        String email = profileObject.get("email").getAsString();

        return connectSocialAccount(1, email);

    }

    //네이버 로그인
    @Override
    public String connectNaverLogin() {
        return "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=" +
                NAVER_CLIENT_ID + "&redirect_uri=" + NAVER_REDIRECT_URI + "&state=" + naverState;
    }

    @Override
    public String getNaverAccessTokenUrl(String code) {
        return "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=" +
                NAVER_CLIENT_ID + "&client_secret=" + NAVER_SECRET + "&redirect_uri=" + NAVER_REDIRECT_URI + "&code=" + code + "&state="
                + naverState;
    }


    @Override
    public HashMap<String, String> getNaverLoginInfo(String accessToken) {

        String header = "Bearer " + accessToken;
        String profileUrl = "https://openapi.naver.com/v1/nid/me";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);

        String responseBody = apiUtil.get(profileUrl, requestHeaders);
        JsonObject profileObject = apiUtil.gson.fromJson(responseBody, JsonObject.class);
        JsonObject responseData = profileObject.get("response").getAsJsonObject();

        String email = responseData.get("email").getAsString();

        return connectSocialAccount(2, email);

    }

    @Override
    public String connectKakaoLogin() {
        return "https://kauth.kakao.com/oauth/authorize?client_id=" + KAKAO_CLIENT_ID + "&redirect_uri=" + KAKAO_REDIRECT_URI
                + "&response_type=code&scope=account_email";
    }

    @Override
    public String getKakaoAccessTokenUrl(String code) {
        return "https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URI + "&code=" + code;
    }

    @Override
    public HashMap<String, String> getKakaoLoginInfo(String accessToken) {

        String header = "Bearer " + accessToken;
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);

        String responseBody = apiUtil.get(userInfoUrl, requestHeaders);
        JsonObject profileObject = apiUtil.gson.fromJson(responseBody, JsonObject.class);
        JsonObject responseData = profileObject.get("kakao_account").getAsJsonObject();

        String email = responseData.get("email").getAsString();

        return connectSocialAccount(3, email);

    }


    @Override
    public HashMap<String, String> connectSocialAccount(int platformType, String email){
        HashMap<String, String> userInfo = new HashMap<>();
        String userId="userId 프론트에서받아오던가";
        if(!(socialLoginRepository.existsByUserIdAndPlatformType(userId, platformType))){
            User user = userRepository.findByUserId(userId);
            if(user != null){
                SocialLogin socialLogin = new SocialLogin(user.getUserId(),platformType,email);
                socialLoginRepository.save(socialLogin);
            }
            else{
                //일반 계정 없으면?
                //회원가입 페이지로 이동해서 회원가입 시켜버림
                User newUser = new User("ggl"+apiUtil.generateRandomUserStr(17),"tempName",email,"tempPassword");
                userRepository.save(newUser); //회원가입시키고
                userId = newUser.getUserId();
                SocialLogin socialLogin = new SocialLogin(userId,platformType,email);
                socialLoginRepository.save(socialLogin);
            }
        }

        userInfo.put("userId", userId);
        userInfo.put("password", (userRepository.findByUserId(userId)).getPassword());

        return userInfo;

    }




}
