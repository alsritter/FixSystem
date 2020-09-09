package com.alsritter.pojo;


import lombok.Data;
import lombok.ToString;

import java.util.Date;

@ToString
@Data
public class Worker {

  private String workId;
  private String name;
  private String gender;
  private String password;
  private Date joinDate;
  private String phone;
  private String details;
  private long ordersNumber;
  private double avgGrade;
  private int state;
}
