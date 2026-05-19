package com.hotpulse.controller.news;

import com.hotpulse.common.Result;
import com.hotpulse.dto.news.NewsCreateDTO;
import com.hotpulse.dto.news.NewsQueryDTO;
import com.hotpulse.entity.news.News;
import com.hotpulse.service.news.NewsService;
import com.hotpulse.vo.news.NewsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public Result<List<NewsVO>> list(@RequestParam(required = false) String category,
                                     @RequestParam(required = false) String keyword) {
        NewsQueryDTO queryDTO = new NewsQueryDTO();
        queryDTO.setCategory(category);
        queryDTO.setKeyword(keyword);
        return Result.success(newsService.list(queryDTO.getCategory(), queryDTO.getKeyword())
                .stream().map(NewsVO::from).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public Result<NewsVO> detail(@PathVariable Long id) {
        return Result.success(NewsVO.from(newsService.getById(id)));
    }

    @PostMapping
    public Result<?> create(@RequestBody NewsCreateDTO dto) {
        News news = new News();
        news.setTitle(dto.getTitle());
        news.setSummary(dto.getSummary());
        news.setSource(dto.getSource());
        news.setCategory(dto.getCategory());
        news.setCoverUrl(dto.getCoverUrl());
        news.setHeat(dto.getHeat());
        news.setPublishedAt(dto.getPublishedAt());
        newsService.save(news);
        return Result.success("created");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        newsService.delete(id);
        return Result.success("deleted");
    }
}
