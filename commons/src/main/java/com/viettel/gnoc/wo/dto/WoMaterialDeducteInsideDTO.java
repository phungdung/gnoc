package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.model.WoMaterialDeducteEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class WoMaterialDeducteInsideDTO extends BaseDto {

  private Long woMaterialDeducteId;
  private Long woId;
  private Long userId;
  private String userName;
  private Long materialId;
  private Long isDeducte;
  private String sendImResult;
  private Date createDate;
  private Date sendImTime;
  private Long actionId;
  private Double quantity;
  private Long reasonId;
  private Double firstMetersIndex;
  private Double lastMetersIndex;
  private Double cableLengthOnNims;
  private String materialGroupCode;
  private Long parentActionId;
  private String materialName;
  private String materialGroupName;
  private String actionName;
  private String unitName;
  private String woCode;
  private String serial;
  private Long type;
  private String nationCode;
  private Long nationId;
  private String defaultSortField;

  public WoMaterialDeducteInsideDTO(Long woMaterialDeducteId, Long woId, Long userId,
      String userName, Long materialId, Long isDeducte, String sendImResult,
      Date createDate, Date sendImTime, Long actionId, Double quantity, Long reasonId,
      Double firstMetersIndex, Double lastMetersIndex, Double cableLengthOnNims, String serial, Long type, String nationCode, Long nationId) {
    this.woMaterialDeducteId = woMaterialDeducteId;
    this.woId = woId;
    this.userId = userId;
    this.userName = userName;
    this.materialId = materialId;
    this.isDeducte = isDeducte;
    this.sendImResult = sendImResult;
    this.createDate = createDate;
    this.sendImTime = sendImTime;
    this.actionId = actionId;
    this.quantity = quantity;
    this.reasonId = reasonId;
    this.firstMetersIndex = firstMetersIndex;
    this.lastMetersIndex = lastMetersIndex;
    this.cableLengthOnNims = cableLengthOnNims;
    this.serial = serial;
    this.type = type;
    this.nationCode = nationCode;
    this.nationId = nationId;
  }

  public WoMaterialDeducteEntity toEntity() {
    return new WoMaterialDeducteEntity(woMaterialDeducteId, woId, userId, userName, materialId,
        isDeducte, sendImResult, createDate, sendImTime, actionId, quantity, reasonId,
        firstMetersIndex, lastMetersIndex, cableLengthOnNims, serial, type, nationCode, nationId);
  }

  public WoMaterialDeducteDTO toModelOutSide() {
    WoMaterialDeducteDTO model = new WoMaterialDeducteDTO(
        StringUtils.validString(woMaterialDeducteId) ? String.valueOf(woMaterialDeducteId) : null,
        StringUtils.validString(woId) ? String.valueOf(woId) : null,
        StringUtils.validString(userId) ? String.valueOf(userId) : null,
        userName,
        StringUtils.validString(materialId) ? String.valueOf(materialId) : null,
        StringUtils.validString(isDeducte) ? String.valueOf(isDeducte) : null,
        sendImResult,
        StringUtils.validString(createDate) ? createDate.toString()
            : null,
        StringUtils.validString(sendImTime) ? sendImTime.toString()
            : null,
        StringUtils.validString(actionId) ? String.valueOf(actionId) : null,
        StringUtils.validString(quantity) ? String.valueOf(quantity.longValue()) : null,
        StringUtils.validString(reasonId) ? String.valueOf(reasonId) : null,
        StringUtils.validString(firstMetersIndex) ? String.valueOf(firstMetersIndex) : null,
        StringUtils.validString(lastMetersIndex) ? String.valueOf(lastMetersIndex) : null,
        StringUtils.validString(cableLengthOnNims) ? String.valueOf(cableLengthOnNims) : null,
        materialGroupCode,
        StringUtils.validString(parentActionId) ? String.valueOf(parentActionId) : null,
        materialName,
        materialGroupName,
        actionName,
        unitName,
        woCode,
        serial,
        StringUtils.validString(type) ? String.valueOf(type) : null,
        nationCode,
        StringUtils.validString(nationId) ? String.valueOf(nationId) : null,
        defaultSortField
    );
    return model;
  }

}
