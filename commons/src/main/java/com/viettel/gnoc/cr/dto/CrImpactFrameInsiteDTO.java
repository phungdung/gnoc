package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.cr.model.CrImpactFrameEntity;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
//@Unique(message = "{validation.cr.impactFrameCode.isDupplicate.CR}", clazz = CrImpactFrameEntity.class, uniqueField = "impactFrameCode", idField = "impactFrameId")

public class CrImpactFrameInsiteDTO extends BaseDto {

  private Long impactFrameId;
  private String impactFrameName;
  @NotNull(message = "{validation.crImpactFrame.null.startTime}")
  private String startTime;
  @NotNull(message = "{validation.crImpactFrame.null.endTime}")
  private String endTime;
  private String description;
  private Long isActive;
  private Long isEditable;
  private List<LanguageExchangeDTO> listCrImpactFrame;
  @NotEmpty(message = "{validation.crImpactFrame.null.impactFrameCode}")
  private String impactFrameCode;
  private Date createdTime;
  private Date updatedTime;
  private String createdUser;
  private String updatedUser;

  public CrImpactFrameInsiteDTO(Long impactFrameId, String impactFrameCode, String impactFrameName,
      String startTime, String endTime, String description, Long isActive, Long isEditable,
      Date createdTime, Date updatedTime, String createdUser, String updatedUser) {
    this.impactFrameId = impactFrameId;
    this.impactFrameCode = impactFrameCode;
    this.impactFrameName = impactFrameName;
    this.startTime = startTime;
    this.endTime = endTime;
    this.description = description;
    this.isActive = isActive;
    this.isEditable = isEditable;
    this.createdTime = createdTime;
    this.updatedTime = updatedTime;
    this.createdUser = createdUser;
    this.updatedUser = updatedUser;
  }

  public CrImpactFrameEntity toEntity() {
    return new CrImpactFrameEntity(
        impactFrameId,
        impactFrameCode,
        impactFrameName,
        startTime,
        endTime,
        description,
        isActive,
        isEditable,
        createdTime,
        updatedTime,
        createdUser,
        updatedUser
    );
  }
}
