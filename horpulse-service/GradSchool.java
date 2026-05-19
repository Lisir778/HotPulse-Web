package com.hotpulse.entity.grad;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("grad_school")
public class GradSchool {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String tag;
    private String city;
    private String province;
    private String website;
    private String logoUrl;
    private String description;
}
