package com.jaypark8282.webFlux.api.v1.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class SignUpResponseDto {

    private Boolean result;
    private Data data;
    private String errorCode;
    private String errorMessage;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {  // static 키워드 추가
        private String userId;
        private String userName;
        private String password;
        private String nickName;
        private String phone;
        private String email;
        private String role;
        private String type;
        private String status;
        private String createId;
        private String createdDateTime;
        private String modifiedId;
        private String modifiedDateTime;
    }
}
