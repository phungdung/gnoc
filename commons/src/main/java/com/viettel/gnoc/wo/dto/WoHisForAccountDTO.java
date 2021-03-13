/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author thanhlv12
 */
@Getter
@Setter
@NoArgsConstructor
public class WoHisForAccountDTO {

  private Long woId;
  private String woCode;
  private String woContent;
  private String woDescription;
  private String createTime;
  private String finishTime;
  private Long reasonLevel1Id;
  private String reasonLevel1Name;
  private Long reasonLevel2Id;
  private String reasonLevel2Name;
  private Long reasonLevel3Id;
  private String reasonLevel3Name;
  private String commentComplete;
  private String customerPhone;
  private List<WoMaterialDeducteDTO> lstMaterial;

}
