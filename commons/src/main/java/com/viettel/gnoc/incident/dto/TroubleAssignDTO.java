package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.incident.model.TroubleAssignEntity;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class TroubleAssignDTO {

  //Fields
  private Long troubleAssignId;
  private Long troubleId;
  private Long assignUnitId;
  private String assignUnitName;
  private Long receiveUnitId;
  private String receiveUnitName;
  private Double timeProcess;
  private Double timeUsed;
  private String status;
  private String content;
  private Date assignTime;
  private Date updateTime;
  private Date completeTime;

  public TroubleAssignDTO(
      Long troubleAssignId, Long troubleId, Long assignUnitId, String assignUnitName,
      Long receiveUnitId, String receiveUnitName, Double timeProcess, Double timeUsed,
      String status, String content,
      Date assignTime, Date updateTime, Date completeTime) {
    this.troubleAssignId = troubleAssignId;
    this.troubleId = troubleId;
    this.assignUnitId = assignUnitId;
    this.assignUnitName = assignUnitName;
    this.receiveUnitId = receiveUnitId;
    this.receiveUnitName = receiveUnitName;
    this.timeProcess = timeProcess;
    this.timeUsed = timeUsed;
    this.status = status;
    this.content = content;
    this.assignTime = assignTime;
    this.updateTime = updateTime;
    this.completeTime = completeTime;
  }

  public TroubleAssignEntity toEntity() {
    TroubleAssignEntity model = new TroubleAssignEntity(
        troubleAssignId,
        troubleId,
        assignUnitId,
        assignUnitName,
        receiveUnitId,
        receiveUnitName,
        timeProcess,
        timeUsed,
        status,
        content,
        assignTime,
        updateTime,
        completeTime
    );
    return model;
  }

}
