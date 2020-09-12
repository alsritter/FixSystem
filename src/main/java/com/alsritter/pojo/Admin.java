package com.alsritter.pojo;


import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class Admin implements User{

  private String id;
  private String name;
  private String gender;
  private String password;
  private Date joinDate;
  private String phone;
  private String details;
}
