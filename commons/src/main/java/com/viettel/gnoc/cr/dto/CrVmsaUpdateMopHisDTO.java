/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.dto;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author tiennv
 */
@Getter
@Setter
@NoArgsConstructor
public class CrVmsaUpdateMopHisDTO {

  private Long crVmsaUpdateMopHisId;
  private Long crId;
  private Long validateKey;
  private String systemCode;
  private Long resultCode;
  private Date createTime;

}
