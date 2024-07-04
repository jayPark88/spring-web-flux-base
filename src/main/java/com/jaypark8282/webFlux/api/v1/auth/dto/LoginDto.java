package com.jaypark8282.webFlux.api.v1.auth.dto;

import lombok.*;


/**
 * com.jaypark8282.core.dto
 * ㄴ LoginDto
 *
 * <pre>
 * description : login에 사용될 dto
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
public class LoginDto {
    private String userName;
    private String password;
}