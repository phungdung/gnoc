package com.viettel.gnoc.wo.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CdInfoForm {

  private String woCode;
  private String userName;
  private String fullName;
  private String mobile;
  private String email;
  private String woId;
  private List<String> lstWoCode;
  private List<Long> lstWoId;
}
