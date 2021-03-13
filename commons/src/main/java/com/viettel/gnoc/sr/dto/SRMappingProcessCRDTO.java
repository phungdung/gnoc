package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.sr.model.SRMappingProcessCREntity;
import groovy.util.logging.Slf4j;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Unique(message = "{validation.SRMappingProcessCRDTO.isDuplicate.serviceCode}", clazz = SRMappingProcessCREntity.class, uniqueField = "serviceCode", idField = "id")
public class SRMappingProcessCRDTO extends BaseDto {

  private Long id;

  @NotEmpty(message = "{validation.SRMappingProcessCRDTO.null.serviceCode}")
  private String serviceCode;
  private Long crProcessParentId;
  private Long crProcessId;
  private String serviceName;
  @NotEmpty(message = "{SRMappingProcessCRDTO.null.process}")
  private String process;
  private String wo;
  private String crProcessParentCode;
  private String crProcessCode;
  private String crProcessName;

  private String ifeId;
  private String ifeCode;
  private String ifeName;
  private String startTime;
  private String endTime;
  private String checkData;
  private String srId;

  private Boolean btnDelete;
  private String resultImport;
  //  dung add
  private Long serviceAffecting;
  private String serviceAffectingStr;
  private String affectingService;
  private Long totalAffectingCustomers;
  private String totalAffectingCustomersStr;
  private Long totalAffectingMinutes;
  private String totalAffectingMinutesStr;
  private Long woFtTypeId;
  private String woFtTypeIdStr;
  private Long groupCDFT;
  private String groupCDFTStr;
  private Long woTestTypeId;
  private String woTestTypeIdStr;
  private Long groupCdFtService;
  private String groupCdFtServiceStr;
  private String woContentService;
  private String woContentCDFT;
  private Long isCrNodes;
  private String isCrNodesStr;
  private Long unitImplement;
  private String unitImplementName;
  private Long dutyType;
  private String dutyTypeStr;
  private Long autoCreateCR;
  private String autoCreateCRStr;
  private String descriptionCr;
  private String typeFindNode;

  private String woFtDescription;
  private String woTestDescription;
  private Date woFtStartTime;
  private String woFtStartTimeStr;
  private Date woFtEndTime;
  private String woFtEndTimeStr;
  private Date woTestStartTime;
  private String woTestStartTimeStr;
  private Date woTestEndTime;
  private String woTestEndTimeStr;
  private Long woFtPriority;
  private String woFtPriorityStr;
  private Long woTestPriority;
  private String woTestPriorityStr;
  private String crTitle;
  private Long crStatus;
  private String crStatusStr;
  private String userNameToken;
  private String processTypeLv3Id;
  private String processTypeLv3IdName;
// end


  public SRMappingProcessCRDTO(String serviceCode) {
    this.serviceCode = serviceCode;
  }

  public SRMappingProcessCRDTO(Long id, String serviceCode,
      Long crProcessParentId, Long crProcessId, Long serviceAffecting,
      String affectingService, Long totalAffectingCustomers, Long totalAffectingMinutes,
      Long woFtTypeId, Long groupCDFT, Long woTestTypeId, Long groupCdFtService,
      String woContentService, String woContentCDFT, Long isCrNodes, Long unitImplement,
      Long dutyType, Long autoCreateCR, String descriptionCr, String woFtDescription,
      String woTestDescription, Date woFtStartTime, Date woFtEndTime, Date woTestStartTime,
      Date woTestEndTime, Long woFtPriority, Long woTestPriority, String crTitle, Long crStatus,
      String typeFindNode, String processTypeLv3Id) {
    this.id = id;
    this.serviceCode = serviceCode;
    this.crProcessParentId = crProcessParentId;
    this.crProcessId = crProcessId;
    this.serviceAffecting = serviceAffecting;
    this.affectingService = affectingService;
    this.totalAffectingCustomers = totalAffectingCustomers;
    this.totalAffectingMinutes = totalAffectingMinutes;
    this.woFtTypeId = woFtTypeId;
    this.groupCDFT = groupCDFT;
    this.woTestTypeId = woTestTypeId;
    this.groupCdFtService = groupCdFtService;
    this.woContentService = woContentService;
    this.woContentCDFT = woContentCDFT;
    this.isCrNodes = isCrNodes;
    this.unitImplement = unitImplement;
    this.dutyType = dutyType;
    this.autoCreateCR = autoCreateCR;
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
    this.typeFindNode = typeFindNode;
    this.processTypeLv3Id = processTypeLv3Id;
  }

  public SRCreateAutoCRDTO toSRCreateAutoCRDTO() {
    return new SRCreateAutoCRDTO(serviceAffecting, affectingService, totalAffectingCustomers,
        totalAffectingMinutes, groupCDFT, woContentCDFT, groupCdFtService, woContentService,
        dutyTypeStr, unitImplement, descriptionCr,
        woFtDescription, woTestDescription, woFtPriority, woTestPriority, crTitle, crStatus,
        woFtTypeId,
        woTestTypeId);
  }

  public SRMappingProcessCREntity toEntity() {
    return new SRMappingProcessCREntity(id, serviceCode,
        crProcessParentId, crProcessId, serviceAffecting, affectingService, totalAffectingCustomers,
        totalAffectingMinutes, woFtTypeId, groupCDFT, woTestTypeId, groupCdFtService,
        woContentService, woContentCDFT, isCrNodes, unitImplement, dutyType, autoCreateCR,
        descriptionCr, woFtDescription, woTestDescription, woFtStartTime, woFtEndTime,
        woTestStartTime, woTestEndTime, woFtPriority, woTestPriority, crTitle, crStatus,
        typeFindNode, processTypeLv3Id);
  }
}
