package com.hotpulse.vo.grad;

import lombok.Data;
import java.util.List;

@Data
public class RecommendVO {
    private String schoolName;
    private String schoolTag;
    private String city;
    private String majorName;
    private String degreeType;
    private String examSubjects;
    private Integer totalScoreLine;    // 复试线
    private Integer avgScore;          // 录取均分
    private Integer applicantCount;    // 报考人数
    private Integer admitCount;        // 录取人数
    private Double admitRatio;         // 报录比
    private String riskLevel;          // 冲/稳/保
    private String reason;             // 推荐理由
    private List<String> tutors;       // 推荐导师
}
