package com.hotpulse.dto.news;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsCreateDTO {
    private String title;
    private String summary;
    private String source;
    private String category;
    private String coverUrl;
    private Long heat;
    private LocalDateTime publishedAt;
}
