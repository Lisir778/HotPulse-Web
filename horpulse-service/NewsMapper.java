package com.hotpulse.mapper.news;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotpulse.entity.news.News;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsMapper extends BaseMapper<News> {
}
