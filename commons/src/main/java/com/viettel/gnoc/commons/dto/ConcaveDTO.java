/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ConcaveDTO {

  private String concavePointCode;
  private String approveStatusName;
  private String cells;
  private Double lat;
  private Double lng;
  private String locationNameFull;
  private String networkTypeName;
  private String statusName;
  private String solutionName;
  private String planProcess;
  private String cabinetCode;
}
