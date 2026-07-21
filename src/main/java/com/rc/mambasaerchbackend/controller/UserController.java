package com.rc.mambasaerchbackend.controller;

import com.rc.mambasaerchbackend.annotation.AuthCheck;
import com.rc.mambasaerchbackend.common.BaseResponse;
import com.rc.mambasaerchbackend.model.dto.*;
import com.rc.mambasaerchbackend.model.entity.User;
import com.rc.mambasaerchbackend.model.vo.UserVO;
import com.rc.mambasaerchbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 */
@Tag(name = "用户管理", description = "用户注册/登录/注销/检索/权限管理")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public BaseResponse<Long> register(@Valid @RequestBody UserRegisterDTO dto) {
        long userId = userService.register(dto.getUsername(), dto.getPassword(), dto.getNickname());
        return BaseResponse.success(userId);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public BaseResponse<UserVO> login(@Valid @RequestBody UserLoginDTO dto) {
        UserVO vo = userService.login(dto.getUsername(), dto.getPassword());
        return BaseResponse.success(vo);
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    @AuthCheck
    public BaseResponse<Void> logout() {
        userService.logout();
        return BaseResponse.success();
    }

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/current")
    @AuthCheck
    public BaseResponse<UserVO> getCurrentUser() {
        return BaseResponse.success(userService.getCurrentUser());
    }

    @Operation(summary = "更新当前用户信息")
    @PutMapping("/update")
    @AuthCheck
    public BaseResponse<Void> updateUser(@Valid @RequestBody UserUpdateDTO dto) {
        User updateParam = new User();
        updateParam.setNickname(dto.getNickname());
        updateParam.setEmail(dto.getEmail());
        updateParam.setPhone(dto.getPhone());
        updateParam.setAvatar(dto.getAvatar());
        updateParam.setPassword(dto.getPassword());
        userService.updateUser(updateParam);
        return BaseResponse.success();
    }

    @Operation(summary = "注销当前用户")
    @DeleteMapping("/delete")
    @AuthCheck
    public BaseResponse<Void> deleteUser() {
        userService.deleteUser();
        return BaseResponse.success();
    }

    // ---------- 管理员接口 ----------

    @Operation(summary = "管理员查询用户列表")
    @GetMapping("/search")
    public BaseResponse<List<UserVO>> searchUsers(UserQueryDTO query) {
        User queryParam = new User();
        if (query != null) {
            queryParam.setUsername(query.getUsername());
            queryParam.setEmail(query.getEmail());
            queryParam.setPhone(query.getPhone());
            queryParam.setRole(query.getRole());
            queryParam.setStatus(query.getStatus());
        }
        return BaseResponse.success(userService.searchUsers(queryParam));
    }

    @Operation(summary = "管理员更新任意用户")
    @PutMapping("/admin/update/{id}")
    @AuthCheck(role = "admin")
    public BaseResponse<Void> adminUpdate(@PathVariable Long id, @RequestBody UserUpdateDTO dto) {
        User updateParam = new User();
        updateParam.setId(id);
        updateParam.setNickname(dto.getNickname());
        updateParam.setEmail(dto.getEmail());
        updateParam.setPhone(dto.getPhone());
        updateParam.setAvatar(dto.getAvatar());
        updateParam.setPassword(dto.getPassword());
        userService.updateUserByAdmin(updateParam);
        return BaseResponse.success();
    }

    @Operation(summary = "管理员删除用户")
    @DeleteMapping("/admin/delete/{id}")
    @AuthCheck(role = "admin")
    public BaseResponse<Void> adminDelete(@PathVariable Long id) {
        userService.deleteUserByAdmin(id);
        return BaseResponse.success();
    }
}
