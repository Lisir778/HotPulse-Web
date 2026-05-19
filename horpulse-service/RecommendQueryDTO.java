package com.hotpulse.dto.grad;

import lombok.Data;

@Data
public class RecommendQueryDTO {
    private String majorKeyword;    // 专业关键词
    private String province;        // 目标省份
    private String schoolTag;       // 院校层次: 985 / 211 / 双一流
    private Integer score;          // 预估分数
    private String degreeType;      // 学硕 / 专硕
}
