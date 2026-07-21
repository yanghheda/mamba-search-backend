package com.rc.mambasaerchbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rc.mambasaerchbackend.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 Mapper（继承 MyBatis-Plus BaseMapper，自动拥有 CRUD 方法）
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询（逻辑删除自动过滤）
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 多条件检索
     */
    List<User> selectByCondition(@Param("query") User query);
}
