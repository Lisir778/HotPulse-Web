package com.hotpulse.service.chat.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hotpulse.entity.chat.ChatHistory;
import com.hotpulse.mapper.chat.ChatMapper;
import com.hotpulse.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMapper chatMapper;

    @Override
    public List<ChatHistory> listByUser(Long userId) {
        return chatMapper.selectList(
                new LambdaQueryWrapper<ChatHistory>().eq(ChatHistory::getUserId, userId).orderByDesc(ChatHistory::getCreatedAt));
    }

    @Override
    public void save(Long userId, String query, String answer) {
        ChatHistory history = new ChatHistory();
        history.setUserId(userId);
        history.setQuery(query);
        history.setAnswer(answer);
        chatMapper.insert(history);
    }
}
