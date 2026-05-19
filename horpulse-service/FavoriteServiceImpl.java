package com.hotpulse.service.favorite.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hotpulse.entity.favorite.Favorite;
import com.hotpulse.mapper.favorite.FavoriteMapper;
import com.hotpulse.service.favorite.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;

    @Override
    public List<Favorite> listByUser(Long userId) {
        return favoriteMapper.selectList(
                new LambdaQueryWrapper<Favorite>().eq(Favorite::getUserId, userId).orderByDesc(Favorite::getCreatedAt));
    }

    @Override
    public void add(Long userId, Long newsId, String type) {
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setNewsId(newsId);
        favorite.setType(type == null || type.isBlank() ? "news" : type);
        favoriteMapper.insert(favorite);
    }

    @Override
    public void delete(Long id) {
        favoriteMapper.deleteById(id);
    }
}
