package com.rc.mambasaerchbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rc.mambasaerchbackend.model.entity.User;
import com.rc.mambasaerchbackend.model.vo.UserVO;

import java.util.List;

/**
 * 用户服务
 */
public interface UserService extends IService<User> {

    /**
     * 注册
     */
    long register(String username, String password, String nickname);

    /**
     * 登录，返回脱敏 VO
     */
    UserVO login(String username, String password);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 获取当前登录用户
     */
    UserVO getCurrentUser();

    /**
     * 更新用户信息（当前登录用户）
     */
    void updateUser(User updateParam);

    /**
     * 管理员：更新任意用户
     */
    void updateUserByAdmin(User updateParam);

    /**
     * 注销（逻辑删除）当前登录用户
     */
    void deleteUser();

    /**
     * 管理员：删除用户
     */
    void deleteUserByAdmin(Long userId);

    /**
     * 条件检索用户
     */
    List<UserVO> searchUsers(User query);
}
