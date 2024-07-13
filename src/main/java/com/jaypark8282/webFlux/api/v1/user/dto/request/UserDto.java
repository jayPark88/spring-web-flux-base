package com.jaypark8282.webFlux.api.v1.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


/**
 * com.jaypark8282.core.dto
 * ㄴ UserDto
 *
 * <pre>
 * description :
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 *  parker, 1.0, 12/25/23  초기작성
 * </pre>
 *
 * @author parker
 * @version 1.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userId;
    private String userName;
    private String password;
    private String nickName;
    private String phone;
    private String email;
    private String role;
    private String type;
}