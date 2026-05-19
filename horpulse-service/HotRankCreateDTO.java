package com.hotpulse.dto.hotrank;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HotRankCreateDTO {
    private Long newsId;
    private String platform;
    private Integer rankNum;
    private Long heatValue;
    private LocalDateTime updatedAt;
}
