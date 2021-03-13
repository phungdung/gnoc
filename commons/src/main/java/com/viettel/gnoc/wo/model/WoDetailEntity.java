package com.viettel.gnoc.wo.model;

import com.viettel.gnoc.wo.dto.WoDetailDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "WO_DETAIL")
public class WoDetailEntity {

  static final long serialVersionUID = 1L;

  @Id
  @Column(name = "WO_ID", unique = true, nullable = false, columnDefinition = "Wo")
  private Long woId;

  @Column(name = "ACCOUNT_ISDN")
  private String accountIsdn;

  @Column(name = "SERVICE_ID")
  private Long serviceId;

  @Column(name = "INFRA_TYPE")
  private Long infraType;

  @Column(name = "CC_SERVICE_ID")
  private Long ccServiceId;

  @Column(name = "CC_GROUP_ID")
  private Long ccGroupId;

  @Column(name = "IS_CC_RESULT")
  private Long isCcResult;

  @Column(name = "CC_RESULT")
  private Long ccResult;

  @Column(name = "CHECK_QOS_INTERNET_RESULT")
  private String checkQosInternetResult;

  @Column(name = "CHECK_QOS_TH_RESULT")
  private String checkQosThResult;

  @Column(name = "CHECK_QR_CODE")
  private String checkQrCode;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATE_DATE")
  private Date createDate;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_TIME")
  private Date lastUpdateTime;

  @Column(name = "TEL_SERVICE_ID")
  private Long telServiceId;

  @Column(name = "PRODUCT_CODE")
  private String productCode;

  @Column(name = "IS_HOT")
  private Long isHot;

  @Column(name = "NUM_HOT")
  private Long numHot;

  @Column(name = "NUM_COMPLAINT")
  private Long numComplaint;

  @Column(name = "CUSTOMER_PHONE")
  private String customerPhone;

  @Column(name = "SPM_CODE")
  private String spmCode;

  @Column(name = "CUSTOMER_NAME")
  private String customerName;

  @Column(name = "SUBSCRIPTION_ID")
  private String subscriptionId;

  @Column(name = "PORT_CORRECT_ID")
  private String portCorrectId;

  @Column(name = "ERR_TYPE_NIMS")
  private String errTypeNims;

  @Column(name = "AUTO_CHECKED")
  private Long autoChecked;

  @Column(name = "CC_PRIORITY_CODE")
  private String ccPriorityCode;

  @Column(name = "MU_CODE")
  private String muCode;

  @Column(name = "RESULT_CHECK")
  private String resultCheck;

  @Column(name = "RESULT_CHECK_NAME")
  private String resultCheckName;

  @Column(name = "INFO_TICKET")
  private String infoTicket;

  @Column(name = "CUSTOMER_GROUP_TYPE")
  private String customerGroupType;

  @Column(name = "KTR_KEY_POINT")
  private String ktrKeyPoint;

  @Column(name = "REASON_REOPEN")
  private String reasonReopen;

  public WoDetailDTO toDTO() {
    return new WoDetailDTO(woId, accountIsdn, serviceId, infraType, ccServiceId, ccGroupId,
        isCcResult, ccResult, checkQosInternetResult, checkQosThResult, checkQrCode, createDate,
        lastUpdateTime, telServiceId, productCode, isHot, numHot, numComplaint, customerPhone,
        spmCode, customerName, subscriptionId, portCorrectId, errTypeNims, autoChecked,
        ccPriorityCode, muCode, resultCheck, resultCheckName, infoTicket, customerGroupType,
        ktrKeyPoint, reasonReopen);
  }

}
