package com.hotpulse.mapper.favorite;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotpulse.entity.favorite.Favorite;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
}
