package com.hotpulse.vo.hotrank;

import com.hotpulse.entity.hotrank.HotRank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HotRankVO {
    private Long id;
    private Long newsId;
    private String platform;
    private Integer rankNum;
    private Long heatValue;
    private LocalDateTime updatedAt;

    public static HotRankVO from(HotRank hotRank) {
        if (hotRank == null) {
            return null;
        }
        HotRankVO vo = new HotRankVO();
        vo.setId(hotRank.getId());
        vo.setNewsId(hotRank.getNewsId());
        vo.setPlatform(hotRank.getPlatform());
        vo.setRankNum(hotRank.getRankNum());
        vo.setHeatValue(hotRank.getHeatValue());
        vo.setUpdatedAt(hotRank.getUpdatedAt());
        return vo;
    }
}
