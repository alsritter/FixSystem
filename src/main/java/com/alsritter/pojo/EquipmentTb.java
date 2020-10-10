package com.alsritter.pojo;


import lombok.Data;
import lombok.ToString;

@Data@ToString
public class EquipmentTb {

  private String ename;
  private long state;
  private long id;
  private String eclass;
  private long egrade;
  private long eweight;
  private java.sql.Timestamp updateDate;
  private long eweightGrade;
  private String eworker;
  private String usingUnit;
  private String etype;

}
