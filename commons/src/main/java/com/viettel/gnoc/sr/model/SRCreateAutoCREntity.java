package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRCreateAutoCRDTO;
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

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "SR_CREATE_AUTO_CR")
public class SRCreateAutoCREntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_CREATE_AUTO_CR_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID")
  private Long id;
  @Column(name = "SR_ID")
  private Long srId;
  @Column(name = "SERVICE_AFFECTING")
  private Long serviceAffecting;
  @Column(name = "AFFECTED_SERVICE")
  private String affectingService;
  @Column(name = "TOTAL_AFFECTED_CUSTOMERS")
  private Long totalAffectingCustomers;
  @Column(name = "TOTAL_AFFECTED_MINUTES")
  private Long totalAffectingMinutes;
  @Column(name = "EXECUTION_TIME")
  private Date executionTime;
  @Column(name = "EXECUTION_END_TIME")
  private Date executionEndTime;
  @Column(name = "COORDINATION_FT")
  private Long coordinationFT;
  @Column(name = "GROUP_CD_FT")
  private Long groupCDFT;
  @Column(name = "TEST_SERVICE")
  private Long testService;
  @Column(name = "GROUP_CD_FT_SERVICE")
  private Long groupCdFtService;
  @Column(name = "FILE_NAME")
  private String fileName;
  @Column(name = "WO_CONTENT_SERVICE")
  private String woContentService;
  @Column(name = "WO_CONTENT_CDFT")
  private String woContentCDFT;
  @Column(name = "PATH_FILE_PROCESS")
  private String pathFileProcess;
  @Column(name = "TIME_ATTACH")
  private Date timeAttach;
  @Column(name = "FILE_TYPE_ID")
  private String fileTypeId;
  @Column(name = "SYNC_STATUS")
  private Long syncStatus;
  @Column(name = "UNIT_IMPLEMENT")
  private Long unitImplement;
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
  @Column(name = "WO_FT_TYPE_ID")
  private Long woFtTypeId;
  @Column(name = "WO_TEST_TYPE_ID")
  private Long woTestTypeId;

  public SRCreateAutoCRDTO toDTO() {
    return new SRCreateAutoCRDTO(id, srId, serviceAffecting, affectingService,
        totalAffectingCustomers, totalAffectingMinutes, executionTime, executionEndTime,
        coordinationFT, groupCDFT
        , testService, groupCdFtService, fileName, woContentService, woContentCDFT, pathFileProcess,
        timeAttach, fileTypeId, syncStatus, unitImplement, descriptionCr, woFtDescription,
        woTestDescription, woFtStartTime, woFtEndTime, woTestStartTime, woTestEndTime, woFtPriority,
        woTestPriority, crTitle, crStatus, woFtTypeId, woTestTypeId);
  }
}
