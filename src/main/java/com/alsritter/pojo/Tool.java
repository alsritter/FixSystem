package com.alsritter.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class Tool {
    private Date createTime;
    private Date updateTime;
    private int updateNumber; // 更新次数
    private float price;    // 单价

    private int toolId;
    private String  toolName;
    private int toolCount;
}
