package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "OPEN_PM", name = "WORK_LOG")
public class WorkLogEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "work_log__seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WORK_LOG_ID", nullable = false)
  private Long workLogId;

  @Column(name = "WLG_OBJECT_TYPE")
  private Long wlgObjectType;

  @Column(name = "WLG_OBJECT_ID")
  private Long wlgObjectId;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "USER_GROUP_ACTION")
  private Long userGroupAction;

  @Column(name = "WLG_TEXT")
  private String wlgText;

  @Column(name = "WLG_EFFORT_HOURS")
  private Long wlgEffortHours;

  @Column(name = "WLG_EFFORT_MINUTES")
  private Long wlgEffortMinutes;

  @Column(name = "WLG_ACCESS_TYPE")
  private Long wlgAccessType;

  @Column(name = "CREATED_DATE")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date createdDate;

  @Column(name = "WLAY_ID")
  private Long wlayId;

  @Column(name = "WLG_OBJECT_STATE")
  private Long wlgObjectState;


  public WorkLogInsiteDTO toDTO() {
    WorkLogInsiteDTO dto = new WorkLogInsiteDTO(
        workLogId
        , wlgObjectType
        , wlgObjectId
        , userId
        , userGroupAction
        , wlgText
        , wlgEffortHours
        , wlgEffortMinutes
        , wlgAccessType
        , createdDate
        , wlayId
        , wlgObjectState
        , null
        , null
    );
    return dto;
  }

}
