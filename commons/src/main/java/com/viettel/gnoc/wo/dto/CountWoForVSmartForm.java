package com.viettel.gnoc.wo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountWoForVSmartForm {

  private String woGroupCode;
  private String woGroupName;
  private Long total;
  private String userName;
  private String summaryStatus;
}
