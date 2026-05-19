package com.hotpulse.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotpulse.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
