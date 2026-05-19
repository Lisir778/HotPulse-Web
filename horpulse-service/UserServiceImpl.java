package com.hotpulse.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hotpulse.common.JwtUtil;
import com.hotpulse.entity.user.User;
import com.hotpulse.mapper.user.UserMapper;
import com.hotpulse.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void register(String username, String password) {
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (count > 0) {
            throw new RuntimeException("username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setAvatar("");
        userMapper.insert(user);
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            throw new RuntimeException("user not found");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("invalid password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("avatar", user.getAvatar());
        return result;
    }

    @Override
    public User getById(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public void updateAvatar(Long userId, String avatar) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("user not found");
        }
        user.setAvatar(avatar);
        userMapper.updateById(user);
    }

    @Override
    public void updateUsername(Long userId, String username) {
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (count > 0) {
            throw new RuntimeException("username already exists");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("user not found");
        }
        user.setUsername(username);
        userMapper.updateById(user);
    }

    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("user not found");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("invalid old password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
    }
}
