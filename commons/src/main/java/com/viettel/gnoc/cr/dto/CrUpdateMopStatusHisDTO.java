package com.viettel.gnoc.cr.dto;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CrUpdateMopStatusHisDTO {

  private Long id;
  private String crNumber;
  private String crStatus;
  private String lstMop;
  private String result;
  private String detail;
  private Date inserTime;

}
