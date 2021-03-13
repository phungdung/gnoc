package com.viettel.gnoc.wfm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WoUpdateStatusFormDTO {

  private Long newStatus;         // trang thai moi
  private String reasonChange;    // nguyen nhan cap nhat trang thai
  private String userChange;      // nhan vien thuc hien cap nhat
  private String finishTime;      // thoi gian dong trong trương hop dong
  private String woCode;          // ma wo
  private String systemChange;    // he thong thay doi
  private Boolean auditFail;
  private Long resultClose;
  private String description;
  private String startTimeNew;
  private String endTimeNew;
}
