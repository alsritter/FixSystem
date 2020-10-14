package com.alsritter.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Admin implements User{

  private String address;
  private String department; // 部门
  private String email;
  private String place;
  private String idnumber;
  private String ground;  // 职位

  private String id;
  private String name;
  private String gender;
  private String password;
  private Date joinDate;
  private String phone;
  private String details;
}
