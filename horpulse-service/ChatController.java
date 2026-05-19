package com.hotpulse.controller.chat;

import com.hotpulse.common.Result;
import com.hotpulse.dto.chat.ChatRequestDTO;
import com.hotpulse.service.chat.ChatService;
import com.hotpulse.vo.chat.ChatHistoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/history")
    public Result<List<ChatHistoryVO>> history(@RequestParam Long userId) {
        return Result.success(chatService.listByUser(userId)
                .stream().map(ChatHistoryVO::from).collect(Collectors.toList()));
    }

    @PostMapping
    public Result<?> create(@RequestBody ChatRequestDTO request) {
        chatService.save(request.getUserId(), request.getQuery(), request.getAnswer());
        return Result.success("created");
    }
}
