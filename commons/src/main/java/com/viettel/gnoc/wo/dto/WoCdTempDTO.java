package com.viettel.gnoc.wo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author itsol
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoCdTempDTO {

  private String woCdTempId;
  private Long userId;
  private String userName;
  private String woGroupId;
  private String woGroupName;
  private String startTime;
  private String endTime;
  private String isCd;
  private String isCdStr;
  private String status;
  private String statusStr;

  private String fullName;
  private String userEmail;
  private String mobileNumber;
  private String woGroupCode;
  private String groupTypeId;

}
