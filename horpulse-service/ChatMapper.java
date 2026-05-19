package com.hotpulse.mapper.chat;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotpulse.entity.chat.ChatHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper extends BaseMapper<ChatHistory> {
}
