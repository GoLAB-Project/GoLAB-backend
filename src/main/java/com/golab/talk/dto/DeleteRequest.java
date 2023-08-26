package com.golab.talk.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteRequest {
    private String userId;
    private String platformType;

}
