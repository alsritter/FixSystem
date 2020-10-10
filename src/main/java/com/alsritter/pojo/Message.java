package com.alsritter.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data@ToString
public class Message {
    private String title;
    private String url;

    private String adminId;
    private Date createDate;
    private long messageId;
    private String messageStr;
}
