
package com.viettel.gnoc.pt.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ITSOL
 */
@Getter
@Setter
public class ProblemMonitorDTO {

  //da qua han tim nguyen nhan goc
  private Long rcaOutOfDatePT;
  //tim nguyen nhan goc trong han
  private Long rcaOnDatePT;
  //tim giai phap tam thoi qua han
  private Long waOutOfDatePT;
  //tim giai phap tam thoi trong han
  private Long waOnDatePT;
  //tim giai phap triet de qua han
  private Long slOutOfDatePT;
  //tim giai phap triet de trong han
  private Long slOnDatePT;
  //cho dong
  private Long clearPT;
}
