package com.alsritter.pojo;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Student {
  private String studentId;
  private String name;
  private String gender;
  private String password;
  private String phone;
}
