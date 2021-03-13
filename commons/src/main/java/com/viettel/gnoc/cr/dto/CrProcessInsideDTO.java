package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.cr.model.CrProcessEntity;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Unique(message = "{validation.crProcess.isDuplicate.crProcessCode}", clazz = CrProcessEntity.class, uniqueField = "crProcessCode", idField = "crProcessId")
public class CrProcessInsideDTO extends BaseDto {

  private Long crProcessId;

  @NotEmpty(message = "{validation.crProcess.null.crProcessCode}")
  @Size(max = 200, message = "validation.crProcess.crProcessCode.tooLong")
  private String crProcessCode;
  @NotEmpty(message = "{validation.crProcess.null.crProcessName}")
  private String crProcessName;
  private String description;
  private Long impactSegmentId;
  private Long deviceTypeId;
  private Long subcategoryId;
  private Long riskLevel;
  private Long impactType;
  private Long crTypeId;
  private Long isActive;
  private Long parentId;
  private Long impactCharacteristic;
  private String otherDept;
  private Long requireMop;
  private Long requireFileLog;
  private Long requireFileTest;
  private Long approvalLevel;
  private Long closeCrWhenResolveSuccess;
  private Long isVmsaActiveCellProcess;
  private String vmsaActiveCellProcessKey;
  private Long isLaneImpact;
  private Long requireApprove;
  private Long crProcessIndex;

  private String impactSegmentName;
  private String deviceTypeName;
  private String deviceTypeCode;
  private String impactSegmentCode;
  private String riskLevelName;
  private String crTypeName;
  private Long crProcessLevel;
  private Boolean isLeaf;
  private String parentCode;
  private String isAdd;
  private String resultImport;
  private String impactTypeName;
  private String impactTimer;
  private String requireFileTestName;
  private String requireFileLogName;
  private String requireMopName;
  private String impactCharacteristicName;
  private String isAddName;
  private String fileCode;
  private Long fileCodeType;
  private String fileCodeTypeName;
  private String fileCodeName;
  private String groupUnitCode;

  private boolean isUpdateParent;

  private List<CrProcessTemplateDTO> listCrProcessTemplate;
  private List<CrProcessDeptGroupDTO> listCrProcessDeptGroup;
  private List<CrProcessWoDTO> listCrProcessWo;
  private List<CrProcessGroup> listCrProcessGroup;
  private List<LanguageExchangeDTO> listCrProcessName;
  //hugntv77 add
  private List<String> listCrProcessTemplateName;
  private List<String> listCrProcessWoName;
  private List<String> listCrProcessDeptGroupName;


  public CrProcessInsideDTO(Long crProcessId, String crProcessCode, String crProcessName,
      String description, Long impactSegmentId, Long deviceTypeId, Long subcategoryId,
      Long riskLevel, Long impactType, Long crTypeId, Long isActive, Long parentId,
      Long impactCharacteristic, String otherDept, Long requireMop, Long requireFileLog,
      Long requireFileTest,
      Long approvalLevel, Long closeCrWhenResolveSuccess, Long isVmsaActiveCellProcess,
      String vmsaActiveCellProcessKey, Long isLaneImpact, Long requireApprove,
      Long crProcessIndex) {
    this.crProcessId = crProcessId;
    this.crProcessCode = crProcessCode;
    this.crProcessName = crProcessName;
    this.description = description;
    this.impactSegmentId = impactSegmentId;
    this.deviceTypeId = deviceTypeId;
    this.subcategoryId = subcategoryId;
    this.riskLevel = riskLevel;
    this.impactType = impactType;
    this.crTypeId = crTypeId;
    this.isActive = isActive;
    this.parentId = parentId;
    this.impactCharacteristic = impactCharacteristic;
    this.otherDept = otherDept;
    this.requireMop = requireMop;
    this.requireFileLog = requireFileLog;
    this.requireFileTest = requireFileTest;
    this.approvalLevel = (approvalLevel == null ? 0 : approvalLevel);
    this.closeCrWhenResolveSuccess = closeCrWhenResolveSuccess;
    this.isVmsaActiveCellProcess = isVmsaActiveCellProcess;
    this.vmsaActiveCellProcessKey = vmsaActiveCellProcessKey;
    this.isLaneImpact = isLaneImpact;
    this.requireApprove = requireApprove;
    this.crProcessIndex = crProcessIndex;
  }

  public CrProcessEntity toEntity() {
    return new CrProcessEntity(crProcessId, crProcessCode, crProcessName,
        description, impactSegmentId,
        deviceTypeId, subcategoryId, riskLevel, impactType, crTypeId,
        isActive, parentId, impactCharacteristic, otherDept, requireMop, requireFileLog,
        requireFileTest,
        approvalLevel,
        closeCrWhenResolveSuccess, isVmsaActiveCellProcess, vmsaActiveCellProcessKey,
        isLaneImpact, requireApprove, crProcessIndex);
  }
}
