package com.rc.mambasaerchbackend.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户响应 VO（脱敏，不返回密码）
 */
@Data
public class UserVO {

    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private String role;
    private Integer status;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
