package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.sr.model.SRCreateAutoCREntity;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class SRCreateAutoCRDTO {

  private Long id;
  private Long srId;
  private Long serviceAffecting;
  private String affectingService;
  private Long totalAffectingCustomers;
  private Long totalAffectingMinutes;
  private Date executionTime;
  private Date executionEndTime;
  private Long coordinationFT;
  private Long groupCDFT;
  private Long testService;
  private Long groupCdFtService;
  private String fileName;
  private String woContentService;
  private String woContentCDFT;
  private String pathFileProcess;
  private Date timeAttach;
  private String fileTypeId;
  private Long syncStatus;

  private List<GnocFileDto> gnocFileDtos;
  private String crProcessId;
  private String tempImportId;
  private String fileType;
  private String name;
  private String userAttach;
  private String defaultSortField;
  private String dutyType;
  private boolean isInsert;
  private SrInsiteDTO srDataUpdate;
  private int indexFile;
  private int indexMappingIp;
  private Long isCrNodes;
  private List<SRCreateAutoCRDTO> lstInforTemplate;

  private List<SRCreateAutoCRDTO> lstFileProcess;
  private List<SRMopDTO> lstMop;

  private String serviceCode;
  private String srUser;
  private String processIdStatus;
  //dung add
  private Long unitImplement;
  private String descriptionCr;
  private String woFtDescription;
  private String woTestDescription;
  private Date woFtStartTime;
  private Date woFtEndTime;
  private Date woTestStartTime;
  private Date woTestEndTime;
  private Long woFtPriority;
  private Long woTestPriority;
  private String crTitle;
  private Long crStatus;
  private Long woFtTypeId;
  private Long woTestTypeId;
//  end

  public SRCreateAutoCRDTO(Long id, Long srId, Long serviceAffecting,
      String affectingService, Long totalAffectingCustomers, Long totalAffectingMinutes,
      Date executionTime, Date executionEndTime, Long coordinationFT, Long groupCDFT,
      Long testService, Long groupCdFtService, String fileName, String woContentService,
      String woContentCDFT, String pathFileProcess, Date timeAttach, String fileTypeId,
      Long syncStatus, Long unitImplement, String descriptionCr, String woFtDescription,
      String woTestDescription, Date woFtStartTime, Date woFtEndTime, Date woTestStartTime,
      Date woTestEndTime, Long woFtPriority, Long woTestPriority, String crTitle, Long crStatus,
      Long woFtTypeId, Long woTestTypeId) {
    this.id = id;
    this.srId = srId;
    this.serviceAffecting = serviceAffecting;
    this.affectingService = affectingService;
    this.totalAffectingCustomers = totalAffectingCustomers;
    this.totalAffectingMinutes = totalAffectingMinutes;
    this.executionTime = executionTime;
    this.executionEndTime = executionEndTime;
    this.coordinationFT = coordinationFT;
    this.groupCDFT = groupCDFT;
    this.testService = testService;
    this.groupCdFtService = groupCdFtService;
    this.fileName = fileName;
    this.woContentService = woContentService;
    this.woContentCDFT = woContentCDFT;
    this.pathFileProcess = pathFileProcess;
    this.timeAttach = timeAttach;
    this.fileTypeId = fileTypeId;
    this.syncStatus = syncStatus;
    this.unitImplement = unitImplement;
    this.descriptionCr = descriptionCr;
    this.woFtDescription = woFtDescription;
    this.woTestDescription = woTestDescription;
    this.woFtStartTime = woFtStartTime;
    this.woFtEndTime = woFtEndTime;
    this.woTestStartTime = woTestStartTime;
    this.woTestEndTime = woTestEndTime;
    this.woFtPriority = woFtPriority;
    this.woTestPriority = woTestPriority;
    this.crTitle = crTitle;
    this.crStatus = crStatus;
    this.woFtTypeId = woFtTypeId;
    this.woTestTypeId = woTestTypeId;
  }

  public SRCreateAutoCRDTO(Long serviceAffecting,
      String affectingService, Long totalAffectingCustomers, Long totalAffectingMinutes,
      Long groupCDFT, String woContentCDFT, Long groupCdFtService, String woContentService,
      String dutyType, Long unitImplement, String descriptionCr, String woFtDescription,
      String woTestDescription, Long woFtPriority, Long woTestPriority, String crTitle, Long crStatus,
      Long woFtTypeId, Long woTestTypeId) {
    this.serviceAffecting = serviceAffecting;
    this.affectingService = affectingService;
    this.totalAffectingCustomers = totalAffectingCustomers;
    this.totalAffectingMinutes = totalAffectingMinutes;
    this.groupCDFT = groupCDFT;
    this.groupCdFtService = groupCdFtService;
    this.woContentService = woContentService;
    this.woContentCDFT = woContentCDFT;
    this.dutyType = dutyType;
    this.unitImplement = unitImplement;
    this.descriptionCr = descriptionCr;
    this.woFtDescription = woFtDescription;
    this.woTestDescription = woTestDescription;
    this.woFtPriority = woFtPriority;
    this.woTestPriority = woTestPriority;
    this.crTitle = crTitle;
    this.crStatus = crStatus;
    this.woFtTypeId = woFtTypeId;
    this.woTestTypeId = woTestTypeId;
  }

  public SRCreateAutoCREntity toEntity() {
    return new SRCreateAutoCREntity(id, srId, serviceAffecting, affectingService,
        totalAffectingCustomers, totalAffectingMinutes, executionTime, executionEndTime,
        coordinationFT, groupCDFT
        , testService, groupCdFtService, fileName, woContentService, woContentCDFT, pathFileProcess,
        timeAttach, fileTypeId, syncStatus, unitImplement, descriptionCr, woFtDescription,
        woTestDescription, woFtStartTime, woFtEndTime, woTestStartTime, woTestEndTime, woFtPriority,
        woTestPriority, crTitle, crStatus, woFtTypeId, woTestTypeId);
  }
}
