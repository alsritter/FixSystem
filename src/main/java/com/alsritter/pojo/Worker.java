package com.alsritter.pojo;


import lombok.ToString;

import java.util.Date;

@ToString
public class Worker {

  private String workId;
  private long uright;
  private String name;
  private String gender;
  private String password;
  private Date joinDate;
  private String phone;
  private String details;
  private long ordersNumber;
  private double avgGrade;


  public String getWorkId() {
    return workId;
  }

  public void setWorkId(String workId) {
    this.workId = workId;
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


  public Date getJoinDate() {
    return joinDate;
  }

  public void setJoinDate(Date joinDate) {
    this.joinDate = joinDate;
  }


  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }


  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }


  public long getOrdersNumber() {
    return ordersNumber;
  }

  public void setOrdersNumber(long ordersNumber) {
    this.ordersNumber = ordersNumber;
  }


  public double getAvgGrade() {
    return avgGrade;
  }

  public void setAvgGrade(double avgGrade) {
    this.avgGrade = avgGrade;
  }

}
