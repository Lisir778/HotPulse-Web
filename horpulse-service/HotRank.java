package com.hotpulse.entity.hotrank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("hot_rank")
public class HotRank {

    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("news_id")
    private Long newsId;
    private String platform;
    @TableField("rank_num")
    private Integer rankNum;
    @TableField("heat_value")
    private Long heatValue;
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
