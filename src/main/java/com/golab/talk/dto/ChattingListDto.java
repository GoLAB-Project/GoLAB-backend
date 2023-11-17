package com.golab.talk.dto;
// 범석 추가
// projection으로 원하는 필드만 DB에서 가져오고 싶을 시,
// Dto를 interface로 선언해야하며
// 가져올 필드명을 get + [필드명 그대로](단, 첫글자는 대문자로) 선언해야 한다.

//import org.springframework.lang.Nullable;


public interface ChattingListDto {
//    @Nullable
    String getProfile_img_url();
    String getName();
    int getRoom_id();
    int getId();
}
