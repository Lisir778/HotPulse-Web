package com.hotpulse.dto.chat;

import lombok.Data;

@Data
public class ChatRequestDTO {
    private Long userId;
    private String query;
    private String answer;
}
