package com.hotpulse.vo.news;

import com.hotpulse.entity.news.News;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsVO {
    private Long id;
    private String title;
    private String summary;
    private String source;
    private String category;
    private String coverUrl;
    private Long heat;
    private LocalDateTime publishedAt;

    public static NewsVO from(News news) {
        if (news == null) {
            return null;
        }
        NewsVO vo = new NewsVO();
        vo.setId(news.getId());
        vo.setTitle(news.getTitle());
        vo.setSummary(news.getSummary());
        vo.setSource(news.getSource());
        vo.setCategory(news.getCategory());
        vo.setCoverUrl(news.getCoverUrl());
        vo.setHeat(news.getHeat());
        vo.setPublishedAt(news.getPublishedAt());
        return vo;
    }
}
