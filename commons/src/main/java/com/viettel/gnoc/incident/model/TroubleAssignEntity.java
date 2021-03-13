package com.viettel.gnoc.incident.model;

import com.viettel.gnoc.incident.dto.TroubleAssignDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "ONE_TM", name = "TROUBLE_ASSIGN")
public class TroubleAssignEntity {

  //Fields
  @Id
  @Column(name = "TROUBLE_ASSIGN_ID", nullable = false)
  private Long troubleAssignId;

  @Column(name = "TROUBLE_ID", nullable = false)
  private Long troubleId;

  @Column(name = "ASSIGN_UNIT_ID")
  private Long assignUnitId;

  @Column(name = "ASSIGN_UNIT_NAME")
  private String assignUnitName;

  @Column(name = "RECEIVE_UNIT_ID")
  private Long receiveUnitId;

  @Column(name = "RECEIVE_UNIT_NAME")
  private String receiveUnitName;

  @Column(name = "TIME_PROCESS")
  private Double timeProcess;

  @Column(name = "TIME_USED")
  private Double timeUsed;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CONTENT")
  private String content;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "ASSIGN_TIME")
  private Date assignTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATE_TIME")
  private Date updateTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "COMPLETE_TIME")
  private Date completeTime;

  public TroubleAssignEntity(Long troubleAssignId, Long troubleId,
      Long assignUnitId, String assignUnitName, Long receiveUnitId, String receiveUnitName,
      Double timeProcess, Double timeUsed, String status, String content, Date assignTime,
      Date updateTime, Date completeTime) {
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

  public TroubleAssignDTO toDTO() {
    TroubleAssignDTO dto = new TroubleAssignDTO(
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
    return dto;
  }
}
