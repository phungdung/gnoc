/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.cr.model.CrProcessEntity;
import com.viettel.gnoc.cr.model.CrProcessWoEntity;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author kienpv
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Unique(message = "{validation.crProcessWo.isDuplicate.woName}", clazz = CrProcessEntity.class, uniqueField = "woName", idField = "crProcessWoId")
public class CrProcessWoDTO {

  private Long crProcessWoId;
  @NotEmpty(message = "{validation.CrProcessWo.null.woName}")
  private String woName;
  private Long woStatus;
  private String description;
  private Long isRequire;
  private Long isRequireCloseWo;
  private Double durationWo;
  private Long crProcessId;
  private String checkBox;
  private Long woTypeId;
  private String woTypeName;
  private Long createWoWhenCloseCR;

  private String crProcessCode;
  private String woTypeCode;
  private String resultImport;
  private String durationWoName;
  private String isRequireName;
  private String isRequireWoName;
  private String createWhenCloseCrName;

  public CrProcessWoDTO(Long crProcessWoId, String woName, Long woStatus,
      String description, Long isRequire, Long isRequireCloseWo, Double durationWo,
      Long crProcessId, Long woTypeId,
      Long createWoWhenCloseCR) {
    this.crProcessWoId = crProcessWoId;
    this.woName = woName;
    this.woStatus = woStatus;
    this.description = description;
    this.isRequire = isRequire;
    this.isRequireCloseWo = isRequireCloseWo;
    this.durationWo = durationWo;
    this.crProcessId = crProcessId;
    this.woTypeId = woTypeId;
    this.createWoWhenCloseCR = createWoWhenCloseCR;
  }

  public CrProcessWoEntity toEntity() {
    return new CrProcessWoEntity(crProcessWoId, woName, woStatus,
        description, isRequire, durationWo, crProcessId, isRequireCloseWo,
        woTypeId, createWoWhenCloseCR);
  }
}
