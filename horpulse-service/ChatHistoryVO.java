package com.hotpulse.vo.chat;

import com.hotpulse.entity.chat.ChatHistory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatHistoryVO {
    private Long id;
    private Long userId;
    private String query;
    private String answer;
    private LocalDateTime createdAt;

    public static ChatHistoryVO from(ChatHistory history) {
        if (history == null) {
            return null;
        }
        ChatHistoryVO vo = new ChatHistoryVO();
        vo.setId(history.getId());
        vo.setUserId(history.getUserId());
        vo.setQuery(history.getQuery());
        vo.setAnswer(history.getAnswer());
        vo.setCreatedAt(history.getCreatedAt());
        return vo;
    }
}
