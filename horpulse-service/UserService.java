package com.hotpulse.service.user;

import com.hotpulse.entity.user.User;

import java.util.Map;

public interface UserService {
    void register(String username, String password);
    Map<String, Object> login(String username, String password);
    User getById(Long userId);
    void updateAvatar(Long userId, String avatar);
    void updateUsername(Long userId, String username);
    void updatePassword(Long userId, String oldPassword, String newPassword);
}
