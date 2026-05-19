package com.hotpulse.entity.grad;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("grad_tutor")
public class GradTutor {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long schoolId;
    private Long majorId;
    private String name;
    private String title;
    private String researchDirection;
    private String email;
}
