package com.alsritter.pojo;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Student implements User{
  private String id;
  private String name;
  private String gender;
  private String password;
  private String phone;
}
