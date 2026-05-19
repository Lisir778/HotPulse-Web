package com.hotpulse.vo.user;

import lombok.Data;

@Data
public class LoginVO {
    private String token;
    private Long userId;
    private String username;
    private String avatar;
}
