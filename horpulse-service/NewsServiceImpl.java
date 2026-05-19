package com.hotpulse.service.news.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hotpulse.entity.news.News;
import com.hotpulse.mapper.news.NewsMapper;
import com.hotpulse.service.news.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsMapper newsMapper;

    @Override
    public List<News> list(String category, String keyword) {
        LambdaQueryWrapper<News> wrapper = new LambdaQueryWrapper<News>()
                .orderByDesc(News::getPublishedAt);

        if (category != null && !category.isBlank()) {
            wrapper.eq(News::getCategory, category);
        }
        if (keyword != null && !keyword.isBlank()) {
            wrapper.like(News::getTitle, keyword);
        }
        return newsMapper.selectList(wrapper);
    }

    @Override
    public News getById(Long id) {
        return newsMapper.selectById(id);
    }

    @Override
    public void save(News news) {
        newsMapper.insert(news);
    }

    @Override
    public void delete(Long id) {
        newsMapper.deleteById(id);
    }
}
