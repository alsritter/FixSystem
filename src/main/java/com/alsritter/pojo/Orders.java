package com.alsritter.pojo;


import lombok.ToString;

import java.util.Date;

@ToString
public class Orders {

  private long fixTableId;
  private String studentId;
  private Date createdTime;
  private String area;
  private String build;
  private String dorm;
  private String phone;
  private String faultClass;
  private String faultEtails;
  private String workId;
  private String adminWorkId;
  private long grade;
  private String massage;
  private long state;


  public long getFixTableId() {
    return fixTableId;
  }

  public void setFixTableId(long fixTableId) {
    this.fixTableId = fixTableId;
  }


  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }


  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }


  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }


  public String getBuild() {
    return build;
  }

  public void setBuild(String build) {
    this.build = build;
  }


  public String getDorm() {
    return dorm;
  }

  public void setDorm(String dorm) {
    this.dorm = dorm;
  }


  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }


  public String getFaultClass() {
    return faultClass;
  }

  public void setFaultClass(String faultClass) {
    this.faultClass = faultClass;
  }


  public String getFaultEtails() {
    return faultEtails;
  }

  public void setFaultEtails(String faultEtails) {
    this.faultEtails = faultEtails;
  }


  public String getWorkId() {
    return workId;
  }

  public void setWorkId(String workId) {
    this.workId = workId;
  }


  public String getAdminWorkId() {
    return adminWorkId;
  }

  public void setAdminWorkId(String adminWorkId) {
    this.adminWorkId = adminWorkId;
  }


  public long getGrade() {
    return grade;
  }

  public void setGrade(long grade) {
    this.grade = grade;
  }


  public String getMassage() {
    return massage;
  }

  public void setMassage(String massage) {
    this.massage = massage;
  }


  public long getState() {
    return state;
  }

  public void setState(long state) {
    this.state = state;
  }

}
