package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.validator.SizeByte;
import com.viettel.gnoc.maintenance.model.MrChecklistsBtsEntity;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MrChecklistsBtsDTO extends BaseDto {

  private Long checklistId;
  @NotNull(message = "validation.mrChecklistsBtsDTO.marketCode.notNull")
  @Size(max = 200, message = "validation.mrChecklistsBtsDTO.marketCode.tooLong")
  private String marketCode;
  @Size(max = 200, message = "validation.mrChecklistsBtsDTO.arrayCode.tooLong")
  private String arrayCode;
  @NotNull(message = "validation.mrChecklistsBtsDTO.deviceType.notNull")
  @Size(max = 200, message = "validation.mrChecklistsBtsDTO.deviceType.tooLong")
  private String deviceType;
  @Size(max = 200, message = "validation.mrChecklistsBtsDTO.materialType.tooLong")
  private String materialType;
  private String createdUser;
  private Date createdTime;
  private String updatedUser;
  private Date updatedTime;
  @NotNull(message = "validation.mrChecklistsBtsDTO.cycle.notNull")
  private Long cycle;
  @SizeByte(max = 200, message = "validation.mrChecklistsBtsDTO.supplierCode.tooLong")
  private String supplierCode;
  private Long imesCheck;

  private List<MrChecklistsBtsDetailDTO> listDetail;
  private Datatable dataDetail;
  private String content;
  private Long photoReq;
  private Long minPhoto;
  private Long maxPhoto;
  private String captureGuide;
  private String marketName;
  private String deviceTypeName;
  private String materialTypeName;
  private String photoReqDisplay;
  private String minPhotoStr;
  private String maxPhotoStr;
  private String cycleStr;
  private String resultImport;
  private Boolean checkExport;
  //trungduong them
  private Long checklistDetailId;
  private String woCode;
  private Long isImportant;
  private Double scoreChecklist;
  private String isImportantName;

  public MrChecklistsBtsDTO(Long checklistId, String marketCode, String arrayCode,
      String deviceType, String materialType, String createdUser, Date createdTime,
      String updatedUser, Date updatedTime, Long cycle, String supplierCode, Long imesCheck) {
    this.checklistId = checklistId;
    this.marketCode = marketCode;
    this.arrayCode = arrayCode;
    this.deviceType = deviceType;
    this.materialType = materialType;
    this.createdUser = createdUser;
    this.createdTime = createdTime;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
    this.cycle = cycle;
    this.supplierCode = supplierCode;
    this.imesCheck = imesCheck;
  }

  public MrChecklistsBtsEntity toEntity() {
    return new MrChecklistsBtsEntity(checklistId, marketCode, arrayCode, deviceType, materialType,
        createdUser, createdTime, updatedUser, updatedTime, cycle, supplierCode, imesCheck);
  }
}
