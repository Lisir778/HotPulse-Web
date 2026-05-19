package com.hotpulse.entity.grad;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("grad_admission")
public class GradAdmission {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long schoolId;
    private Long majorId;
    private Integer year;
    private String examSubjects;
    private Integer totalScoreLine;
    private Integer politicalLine;
    private Integer englishLine;
    private Integer majorLine1;
    private Integer majorLine2;
    private Integer applicantCount;
    private Integer admitCount;
    private Integer minScore;
    private Integer avgScore;
}
