package com.alsritter.pojo;

import lombok.ToString;

@ToString
public class Student {

  private String studentId;
  private long uright;
  private String name;
  private String gender;
  private String password;
  private String phone;


  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }


  public long getUright() {
    return uright;
  }

  public void setUright(long uright) {
    this.uright = uright;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

}
