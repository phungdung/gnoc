package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRMappingProcessCRDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author KienPV
 */
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "OPEN_PM", name = "SR_MAPPING_PROCESS_CR")
public class SRMappingProcessCREntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "open_pm.SR_MAPPING_PROCESS_CR_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "SERVICE_CODE")
  private String serviceCode;

  @Column(name = "CR_PROCESS_PARENT_ID")
  private Long crProcessParentId;

  @Column(name = "CR_PROCESS_ID")
  private Long crProcessId;

  @Column(name = "SERVICE_AFFECTING")
  private Long serviceAffecting;

  @Column(name = "AFFECTED_SERVICE")
  private String affectingService;

  @Column(name = "TOTAL_AFFECTED_CUSTOMERS")
  private Long totalAffectingCustomers;

  @Column(name = "TOTAL_AFFECTED_MINUTES")
  private Long totalAffectingMinutes;

  @Column(name = "WO_FT_TYPE_ID")
  private Long woFtTypeId;

  @Column(name = "GROUP_CD_FT")
  private Long groupCDFT;

  @Column(name = "WO_TEST_TYPE_ID")
  private Long woTestTypeId;

  @Column(name = "GROUP_CD_FT_SERVICE")
  private Long groupCdFtService;

  @Column(name = "WO_CONTENT_SERVICE")
  private String woContentService;

  @Column(name = "WO_CONTENT_CDFT")
  private String woContentCDFT;

  @Column(name = "IS_CR_NODES")
  private Long isCrNodes;

  @Column(name = "UNIT_IMPLEMENT")
  private Long unitImplement;

  @Column(name = "DUTY_TYPE")
  private Long dutyType;

  @Column(name = "AUTO_CREATE_CR")
  private Long autoCreateCR;

  @Column(name = "CR_DESCRIPTION")
  private String descriptionCr;

  @Column(name = "WO_FT_DESCRIPTION")
  private String woFtDescription;

  @Column(name = "WO_TEST_DESCRIPTION")
  private String woTestDescription;

  @Column(name = "WO_FT_START_TIME")
  private Date woFtStartTime;

  @Column(name = "WO_FT_END_TIME")
  private Date woFtEndTime;

  @Column(name = "WO_TEST_START_TIME")
  private Date woTestStartTime;

  @Column(name = "WO_TEST_END_TIME")
  private Date woTestEndTime;

  @Column(name = "WO_FT_PRIORITY")
  private Long woFtPriority;

  @Column(name = "WO_TEST_PRIORITY")
  private Long woTestPriority;

  @Column(name = "CR_TITLE")
  private String crTitle;

  @Column(name = "CR_STATUS")
  private Long crStatus;

  @Column(name = "TYPE_FIND_NODE")
  private String typeFindNode;

  @Column(name = "PROCESS_TYPE_LV3_ID")
  private String processTypeLv3Id;

  public SRMappingProcessCRDTO toDTO() {
    return new SRMappingProcessCRDTO(id, serviceCode,
        crProcessParentId, crProcessId, serviceAffecting, affectingService, totalAffectingCustomers,
        totalAffectingMinutes, woFtTypeId, groupCDFT, woTestTypeId, groupCdFtService,
        woContentService, woContentCDFT, isCrNodes, unitImplement, dutyType, autoCreateCR,
        descriptionCr, woFtDescription, woTestDescription, woFtStartTime, woFtEndTime,
        woTestStartTime, woTestEndTime, woFtPriority, woTestPriority, crTitle, crStatus,
        typeFindNode,processTypeLv3Id);
  }
}
