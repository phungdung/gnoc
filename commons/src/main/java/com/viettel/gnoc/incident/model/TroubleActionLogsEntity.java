package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.TroubleActionLogsDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "ONE_TM", name = "TROUBLE_ACTION_LOGS")
public class TroubleActionLogsEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TROUBLE_ACTION_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "CONTENT", nullable = false)
  private String content;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATE_TIME", nullable = false)
  private Date createTime;

  @Column(name = "CREATE_UNIT_ID", nullable = false)
  private Long createUnitId;

  @Column(name = "CREATE_USER_ID")
  private Long createUserId;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "TROUBLE_ID", nullable = false)
  private Long troubleId;

  @Column(name = "CREATER_UNIT_NAME")
  private String createrUnitName;

  @Column(name = "CREATER_USER_NAME")
  private String createrUserName;

  @Column(name = "STATE_ID", nullable = false)
  private Long stateId;

  @Column(name = "TROUBLE_CODE")
  private String troubleCode;

  @Column(name = "STATE_NAME")
  private String stateName;

  @Column(name = "INSERT_SOURCE")
  private String insertSource;

  @Column(name = "ACTION_INFO")
  private String actionInfo;

  //luu du thua phuc vu cc
  @Column(name = "ROOT_CAUSE")
  private String rootCause;

  @Column(name = "WORK_ARROUND")
  private String workArround;

  //Dunglv3 add colum
  @Column(name = "INFOR_MATION")
  private String inforMation;

  public TroubleActionLogsEntity(
      Long id, String content, Date createTime, Long createUnitId, Long createUserId, String type,
      Long troubleId, String createrUnitName, String createrUserName, Long stateId,
      String troubleCode, String stateName, String actionInfo, String insertSource) {
    this.id = id;
    this.content = content;
    this.createTime = createTime;
    this.createUnitId = createUnitId;
    this.createUserId = createUserId;
    this.type = type;
    this.troubleId = troubleId;
    this.createrUnitName = createrUnitName;
    this.createrUserName = createrUserName;
    this.stateId = stateId;
    this.troubleCode = troubleCode;
    this.stateName = stateName;
    this.insertSource = insertSource;
    this.actionInfo = actionInfo;
  }

  public TroubleActionLogsEntity(
      Long id, String content, Date createTime, Long createUnitId, Long createUserId, String type,
      Long troubleId, String createrUnitName, String createrUserName, Long stateId,
      String troubleCode, String stateName, String actionInfo, String insertSource, String inforMation) {
    this.id = id;
    this.content = content;
    this.createTime = createTime;
    this.createUnitId = createUnitId;
    this.createUserId = createUserId;
    this.type = type;
    this.troubleId = troubleId;
    this.createrUnitName = createrUnitName;
    this.createrUserName = createrUserName;
    this.stateId = stateId;
    this.troubleCode = troubleCode;
    this.stateName = stateName;
    this.insertSource = insertSource;
    this.actionInfo = actionInfo;
    this.inforMation = inforMation;
  }

  public TroubleActionLogsDTO toDTO() {
    TroubleActionLogsDTO dto = new TroubleActionLogsDTO(
        id,
        content,
        createTime,
        createUnitId,
        createUserId,
        type, troubleId,
        createrUnitName, createrUserName,
        stateId,
        troubleCode,
        stateName,
        actionInfo,
        insertSource,
        inforMation //add
    );
    dto.setRootCause(rootCause);
    dto.setWorkArround(workArround);
    return dto;
  }
}
