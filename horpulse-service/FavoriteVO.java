package com.hotpulse.vo.favorite;

import com.hotpulse.entity.favorite.Favorite;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FavoriteVO {
    private Long id;
    private Long userId;
    private Long newsId;
    private String type;
    private LocalDateTime createdAt;

    public static FavoriteVO from(Favorite favorite) {
        if (favorite == null) {
            return null;
        }
        FavoriteVO vo = new FavoriteVO();
        vo.setId(favorite.getId());
        vo.setUserId(favorite.getUserId());
        vo.setNewsId(favorite.getNewsId());
        vo.setType(favorite.getType());
        vo.setCreatedAt(favorite.getCreatedAt());
        return vo;
    }
}
