package com.rc.mambasaerchbackend.model.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户更新请求
 */
@Data
public class UserUpdateDTO {

    @Size(max = 20, message = "昵称最多20个字符")
    private String nickname;

    @Size(max = 100, message = "邮箱最多100个字符")
    private String email;

    @Size(max = 20, message = "手机号最多20个字符")
    private String phone;

    private String avatar;

    private String password;
}
