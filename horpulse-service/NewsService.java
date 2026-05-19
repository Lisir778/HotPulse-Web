package com.hotpulse.service.news;

import com.hotpulse.entity.news.News;

import java.util.List;

public interface NewsService {
    List<News> list(String category, String keyword);
    News getById(Long id);
    void save(News news);
    void delete(Long id);
}
