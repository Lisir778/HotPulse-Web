package com.hotpulse.service.chat;

import com.hotpulse.entity.chat.ChatHistory;

import java.util.List;

public interface ChatService {
    List<ChatHistory> listByUser(Long userId);
    void save(Long userId, String query, String answer);
}
