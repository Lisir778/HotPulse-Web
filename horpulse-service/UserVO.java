package com.hotpulse.vo.user;

import com.hotpulse.entity.user.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String avatar;
    private LocalDateTime createdAt;

    public static UserVO from(User user) {
        if (user == null) {
            return null;
        }
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setAvatar(user.getAvatar());
        vo.setCreatedAt(user.getCreatedAt());
        return vo;
    }
}
