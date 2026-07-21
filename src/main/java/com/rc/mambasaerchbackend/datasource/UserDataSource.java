package com.rc.mambasaerchbackend.datasource;

import com.rc.mambasaerchbackend.model.entity.User;
import com.rc.mambasaerchbackend.model.vo.UserVO;
import com.rc.mambasaerchbackend.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserDataSource implements DataSource<UserVO> {

    @Resource
    private UserService userService;

    @Override
    public List<UserVO> doSearch(String searchText) {
        User user = new User();
        user.setUsername(searchText);
        return userService.searchUsers(user);
    }
}
