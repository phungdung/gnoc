/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wfm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ITSOL
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WoTypeTimeDTO {

  private Long woTypeTimeId;
  private Long woTypeId;
  private Double duration;
  private Long isImmediate;
  private Long isImmediateStr;
  private Long userApprovePending;
  private Long userApprovePendingStr;
  private Long waitForApprovePending;
  private Long waitForApprovePendingStr;

}
