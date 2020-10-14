package com.alsritter.pojo;


import lombok.Data;
import lombok.ToString;

import java.util.Date;

@ToString
@Data
public class Orders {

  private Integer eid; // 设备 id(这里要用 Integer)
  private long fixTableId;
  private String studentId;
  private String contacts;
  private String address;
  private Date createdTime;
  private Date endTime;
  private String phone;
  private String faultClass;
  private String faultDetail;
  private String workId;
  private String adminWorkId;
  private String resultDetails;
  private long grade;
  private String message;
  private String urls;
  private long state;
}
