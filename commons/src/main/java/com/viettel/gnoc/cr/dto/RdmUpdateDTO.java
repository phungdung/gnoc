/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RdmUpdateDTO {

  private String taskId;
  private String gnocTaskCode;
  private String gnocTaskName;
  private String gnocTaskType;
  private String gnocTaskFromDate;
  private String gnocTaskToDate;
  private String gnocTaskStatus;
  private String gnocTaskUserPic;
  private String gnocId;

}
