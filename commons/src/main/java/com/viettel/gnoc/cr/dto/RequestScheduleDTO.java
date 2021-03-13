package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.ScheduleCRFormDTO;
import com.viettel.gnoc.cr.model.RequestScheduleEntity;
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
public class RequestScheduleDTO extends BaseDto {

  private Long idSchedule;
  @NotNull(message = "{validation.requestSchedule.null.unitId}")
  private Long unitId;
  private Long status;
  private Date startDate;
  private Date endDate;
  private Date createdDate;
  private String createdUser;
  @NotEmpty(message = "{validation.requestSchedule.null.type}")
  private String type;
  @NotNull(message = "{validation.requestSchedule.null.workTime}")
  private Long workTime;
  private Long complicateWork;
  private Long sameNode;
  private Long sameService;
  private Long sameNodeShift;
  private Long sameServiceShift;

  private String unitName;

  private Date startDateFrom;
  private Date startDateTo;

  private Date endDateFrom;
  private Date endDateTo;

  private Date createdDateFrom;
  private Date createdDateTo;

  private Date dayFrom;
  private Date dayTo;

  //Insert
  private String week;
  private String month;
  private String year;
  private String days;
  //++
  private String detail;
  private String detailDisplay;
  private String typeDisplay;
  //AnhLP add
  private Date pdfDay;
  private String message;
  private List<ScheduleCRFormDTO> scheduleCRFormDTOS;
  private List<ScheduleEmployeeDTO> scheduleEmployeeDTOS;
  private List<ScheduleCRDTO> crAfterList;

  private Long isGetCrBefore;
  private Long isInsert;

  public RequestScheduleDTO(Long idSchedule, Long unitId, Long status, Date startDate, Date endDate,
      String createdUser, Date createdDate, String type,
      Long workTime, Long complicateWork, Long sameNode, Long sameService, Long sameNodeShift,
      Long sameServiceShift, String detail) {
    this.idSchedule = idSchedule;
    this.unitId = unitId;
    this.status = status;
    this.startDate = startDate;
    this.endDate = endDate;
    this.createdUser = createdUser;
    this.createdDate = createdDate;
    this.type = type;
    this.workTime = workTime;
    this.complicateWork = complicateWork;
    this.sameNode = sameNode;
    this.sameService = sameService;
    this.sameNodeShift = sameNodeShift;
    this.sameServiceShift = sameServiceShift;
    this.detail = detail;
  }

  public RequestScheduleEntity toEntity() {
    return new RequestScheduleEntity(
        idSchedule,
        unitId,
        status,
        startDate,
        endDate,
        createdUser,
        createdDate,
        type,
        detail,
        workTime,
        complicateWork,
        sameNode,
        sameService,
        sameNodeShift,
        sameServiceShift
    );
  }
}
