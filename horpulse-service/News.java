package com.hotpulse.entity.news;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("news")
public class News {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String summary;
    private String source;
    private String category;
    @TableField("cover_url")
    private String coverUrl;
    private Long heat;
    @TableField("published_at")
    private LocalDateTime publishedAt;
}
