package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.ShiftHandoverEntity;
import com.viettel.gnoc.cr.dto.ShiftCrDTO;
import com.viettel.gnoc.cr.dto.ShiftItDTO;
import com.viettel.gnoc.cr.dto.ShiftItSeriousDTO;
import com.viettel.gnoc.cr.dto.ShiftStaftDTO;
import com.viettel.gnoc.cr.dto.ShiftWorkDTO;
import com.viettel.gnoc.cr.dto.ShiftWorkOtherDTO;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftHandoverDTO extends BaseDto {

  private Long id;
  private String userName;
  private Long userId;
  private String unitName;
  private Long unitId;
  private Long shiftId;
  private Date lastUpdateTime;
  private Long status;
  private Date createdTime;
  private Date createdTimeOld;
  private String shiftName;
  private String shiftValue;
  private String statusName;

  private List<ShiftStaftDTO> shiftStaftDTOList;
  private List<ShiftWorkDTO> shiftWorkDTOList;
  private List<ShiftItSeriousDTO> shiftItSeriousDTOList;
  private List<ShiftItDTO> shiftItDTOList;
  private List<ShiftCrDTO> shiftCrDTOList;
  private List<ShiftWorkOtherDTO> shiftWorkOtherDTOList;
  private List<GnocFileDto> gnocFileDtos;
  private Date createTimeFrom;
  private Date createTimeTo;
  private Date lastUpdateTimeFrom;
  private Date lastUpdateTimeTo;
  private Long openToolTip;
  private List<Long> idDeleteList;

  public ShiftHandoverDTO(Long id, String userName,
      Long userId, String unitName, Long unitId, Long shiftId,
      Date lastUpdateTime, Long status, Date createdTime) {
    this.id = id;
    this.userName = userName;
    this.userId = userId;
    this.unitName = unitName;
    this.unitId = unitId;
    this.shiftId = shiftId;
    this.lastUpdateTime = lastUpdateTime;
    this.status = status;
    this.createdTime = createdTime;
  }

  public ShiftHandoverEntity toEntity() {
    return new ShiftHandoverEntity(id, userName, userId, unitName, unitId, shiftId, lastUpdateTime,
        status, createdTime);
  }
}
