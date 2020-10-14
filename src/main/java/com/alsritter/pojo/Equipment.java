package com.alsritter.pojo;


import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data@ToString
public class Equipment {

  private String ename;
  private int state;
  private int id;
  private String eclass;
  private int egrade;
  private int eweight;
  private Date updateDate;
  private int eweightGrade;
  private String eworker;
  private String usingUnit;
  private String etype;
  private String url;
  private String address;
}
