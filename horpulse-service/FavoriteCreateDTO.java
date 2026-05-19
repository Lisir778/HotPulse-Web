package com.hotpulse.dto.favorite;

import lombok.Data;

@Data
public class FavoriteCreateDTO {
    private Long userId;
    private Long newsId;
    private String type;
}
