package com.rc.mambasaerchbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rc.mambasaerchbackend.common.BusinessException;
import com.rc.mambasaerchbackend.common.ErrorCode;
import com.rc.mambasaerchbackend.mapper.UserMapper;
import com.rc.mambasaerchbackend.model.entity.User;
import com.rc.mambasaerchbackend.model.vo.UserVO;
import com.rc.mambasaerchbackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    private static final String SALT = "template_salt";

    private final UserMapper userMapper;
    private final HttpServletRequest request;

    public UserServiceImpl(UserMapper userMapper, HttpServletRequest request) {
        this.userMapper = userMapper;
        this.request = request;
    }

    @Override
    public long register(String username, String password, String nickname) {
        // 检查用户名是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(encrypt(password));
        user.setNickname(nickname != null ? nickname : username);
        user.setRole("user");
        user.setStatus(1);

        userMapper.insert(user);
        return user.getId();
    }

    @Override
    public UserVO login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        if (user.getStatus() == 0) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "用户已被禁用");
        }
        if (!user.getPassword().equals(encrypt(password))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }

        // 更新最后登录时间（使用 LambdaUpdateWrapper 精确更新）
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, user.getId())
                .set(User::getLastLoginTime, LocalDateTime.now());
        userMapper.update(null, updateWrapper);

        // 写入 Session
        HttpSession session = request.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userRole", user.getRole());

        return toVO(user);
    }

    @Override
    public void logout() {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @Override
    public UserVO getCurrentUser() {
        User user = userMapper.selectById(getLoginUserId());
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return toVO(user);
    }

    @Override
    public void updateUser(User updateParam) {
        updateParam.setId(getLoginUserId());
        if (updateParam.getPassword() != null) {
            updateParam.setPassword(encrypt(updateParam.getPassword()));
        }
        userMapper.updateById(updateParam);
    }

    @Override
    public void updateUserByAdmin(User updateParam) {
        if (updateParam.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }
        if (updateParam.getPassword() != null) {
            updateParam.setPassword(encrypt(updateParam.getPassword()));
        }
        userMapper.updateById(updateParam);
    }

    @Override
    public void deleteUser() {
        Long userId = getLoginUserId();
        userMapper.deleteById(userId); // 按逻辑删除
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @Override
    public void deleteUserByAdmin(Long userId) {
        userMapper.deleteById(userId); // 按逻辑删除
    }

    @Override
    public List<UserVO> searchUsers(User query) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getUsername()), User::getUsername, query.getUsername())
               .like(StringUtils.hasText(query.getEmail()), User::getEmail, query.getEmail())
               .eq(StringUtils.hasText(query.getPhone()), User::getPhone, query.getPhone())
               .eq(StringUtils.hasText(query.getRole()), User::getRole, query.getRole())
               .eq(query.getStatus() != null, User::getStatus, query.getStatus())
               .orderByDesc(User::getCreatedAt);

        List<User> users = userMapper.selectList(wrapper);
        return users.stream().map(this::toVO).toList();
    }

    // ---------- 工具方法 ----------

    private Long getLoginUserId() {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return userId;
    }

    private String encrypt(String password) {
        return DigestUtils.md5DigestAsHex((SALT + password).getBytes(StandardCharsets.UTF_8));
    }

    private UserVO toVO(User user) {
        if (user == null) return null;
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
