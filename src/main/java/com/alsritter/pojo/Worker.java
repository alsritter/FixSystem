package com.alsritter.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Worker implements User{
  private String address;
  private String department;
  private String email;
  private String place;
  private String idnumber;
  private String ground;

  private String id;
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
