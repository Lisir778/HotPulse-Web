package com.hotpulse.service.favorite;

import com.hotpulse.entity.favorite.Favorite;

import java.util.List;

public interface FavoriteService {
    List<Favorite> listByUser(Long userId);
    void add(Long userId, Long newsId, String type);
    void delete(Long id);
}
