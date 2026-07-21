package com.rc.mambasaerchbackend.model.dto;

import lombok.Data;

/**
 * 用户查询请求
 */
@Data
public class UserQueryDTO {

    private String username;
    private String email;
    private String phone;
    private String role;
    private Integer status;
}
