/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ITSOL
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KpiCompleteVsmartResult {

  private String key;
  private String message;
  private Long totalOnWeb;
  private Long totalOnVsmart;
  private List<KpiCompleteVsamrtForm> lstData;


}
