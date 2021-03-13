package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.WoDetailEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoDetailDTO extends BaseDto {

  private Long woId;
  private String accountIsdn;
  private Long serviceId;
  private Long infraType;
  private Long ccServiceId;
  private Long ccGroupId;
  private Long isCcResult;
  private Long ccResult;
  private String checkQosInternetResult;
  private String checkQosThResult;
  private String checkQrCode;
  private Date createDate;
  private Date lastUpdateTime;
  private Long telServiceId;
  private String productCode;
  private Long isHot;
  private Long numHot;
  private Long numComplaint;
  private String customerPhone;
  private String spmCode;
  private String customerName;
  private String subscriptionId;
  private String portCorrectId;
  private String errTypeNims;
  private Long autoChecked;
  private String ccPriorityCode;
  private String muCode;

  private String resultCheck;
  private String resultCheckName;
  private String infoTicket;

  private String woCode;

  //tiennv them update WO
  private String customerGroupType;
  private String ktrKeyPoint;
  private String reasonReopen;

  public WoDetailDTO(Long woId, String accountIsdn, Long serviceId, Long infraType,
      Long ccServiceId, Long ccGroupId, Long isCcResult, Long ccResult,
      String checkQosInternetResult, String checkQosThResult, String checkQrCode,
      Date createDate, Date lastUpdateTime, Long telServiceId, String productCode,
      Long isHot, Long numHot, Long numComplaint, String customerPhone, String spmCode,
      String customerName, String subscriptionId, String portCorrectId, String errTypeNims,
      Long autoChecked, String ccPriorityCode, String muCode, String resultCheck,
      String resultCheckName, String infoTicket, String customerGroupType, String ktrKeyPoint,
      String reasonReopen) {
    this.woId = woId;
    this.accountIsdn = accountIsdn;
    this.serviceId = serviceId;
    this.infraType = infraType;
    this.ccServiceId = ccServiceId;
    this.ccGroupId = ccGroupId;
    this.isCcResult = isCcResult;
    this.ccResult = ccResult;
    this.checkQosInternetResult = checkQosInternetResult;
    this.checkQosThResult = checkQosThResult;
    this.checkQrCode = checkQrCode;
    this.createDate = createDate;
    this.lastUpdateTime = lastUpdateTime;
    this.telServiceId = telServiceId;
    this.productCode = productCode;
    this.isHot = isHot;
    this.numHot = numHot;
    this.numComplaint = numComplaint;
    this.customerPhone = customerPhone;
    this.spmCode = spmCode;
    this.customerName = customerName;
    this.subscriptionId = subscriptionId;
    this.portCorrectId = portCorrectId;
    this.errTypeNims = errTypeNims;
    this.autoChecked = autoChecked;
    this.ccPriorityCode = ccPriorityCode;
    this.muCode = muCode;
    this.resultCheck = resultCheck;
    this.resultCheckName = resultCheckName;
    this.infoTicket = infoTicket;
    this.customerGroupType = customerGroupType;
    this.ktrKeyPoint = ktrKeyPoint;
    this.reasonReopen = reasonReopen;
  }

  public WoDetailEntity toEntity() {
    return new WoDetailEntity(woId, accountIsdn, serviceId, infraType, ccServiceId, ccGroupId,
        isCcResult, ccResult, checkQosInternetResult, checkQosThResult, checkQrCode, createDate,
        lastUpdateTime, telServiceId, productCode, isHot, numHot, numComplaint, customerPhone,
        spmCode, customerName, subscriptionId, portCorrectId, errTypeNims, autoChecked,
        ccPriorityCode, muCode, resultCheck, resultCheckName, infoTicket, customerGroupType,
        ktrKeyPoint, reasonReopen);
  }
}
