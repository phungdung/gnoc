/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.od.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ITSOL
 */
@Getter
@Setter
public class OdChangeStatusForm {

  private String odCode;
  private String systemChange;
  private String reasonChange;
  private String userChange;
  private String status;
  private String closeTime;
  private String odId;
  private String isClose;
  private String finishTime;
}
