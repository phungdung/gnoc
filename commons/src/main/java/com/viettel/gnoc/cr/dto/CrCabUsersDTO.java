package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.CrCabUsersEntity;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrCabUsersDTO extends BaseDto {

  private Long crCabUsersId;
  @NotNull(message = "{validation.crCabUsersDTO.impactSegmentId.NotNull}")
  private Long impactSegmentId;
  @NotNull(message = "{validation.crCabUsersDTO.executeUnitId.NotNull}")
  private Long executeUnitId;
  @NotNull(message = "{validation.crCabUsersDTO.cabUnitId.NotNull}")
  private Long cabUnitId;
  @NotNull(message = "{validation.crCabUsersDTO.userID.NotNull}")
  private Long userID;
  private Long creationUnitId;
  private String segmentName;
  private String cabUnitName;
  private String executeUnitName;
  private String userFullName;
  private String assignMonth;
  private String assignYear;
  private String countCR;
  private String creationUnitName;

  public CrCabUsersDTO(Long crCabUsersId, Long impactSegmentId, Long executeUnitId, Long cabUnitId,
      Long userID, Long creationUnitId) {
    this.crCabUsersId = crCabUsersId;
    this.impactSegmentId = impactSegmentId;
    this.executeUnitId = executeUnitId;
    this.cabUnitId = cabUnitId;
    this.userID = userID;
    this.creationUnitId = creationUnitId;
  }

  public CrCabUsersEntity toEntity() {
    return new CrCabUsersEntity(crCabUsersId, impactSegmentId, executeUnitId,
        cabUnitId, userID, creationUnitId);
  }
}
