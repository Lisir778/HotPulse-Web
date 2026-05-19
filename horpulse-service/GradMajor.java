package com.hotpulse.entity.grad;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("grad_major")
public class GradMajor {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String category;
    private String degreeType;
    private String code;
}
