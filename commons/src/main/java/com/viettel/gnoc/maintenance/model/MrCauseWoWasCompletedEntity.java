package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.maintenance.dto.MrCauseWoWasCompletedDTO;
import com.viettel.gnoc.maintenance.dto.MrSchedulePeriodicDTO;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "CAUSE_WO_WAS_COMPLETED")
public class MrCauseWoWasCompletedEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CAUSE_WO_WAS_COMPLETED_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "REASON_CODE")
  private String reasonCode;

  @Column(name = "REASON_NAME")
  private String reasonName;

  @Column(name = "REASON_TYPE")
  private String reasonType;

  @Column(name = "WAITING_TIME")
  private Double waitingTime;

  @Column(name = "VALIDATE_PROCESS")
  private String validateProcess;

  @Column(name = "UPDATED_USER")
  private String updatedUser;

  @Column(name = "UPDATED_TIME")
  private Date updatedTime;

  public MrCauseWoWasCompletedDTO toDTO() {
    MrCauseWoWasCompletedDTO dto = new MrCauseWoWasCompletedDTO(id, reasonCode, reasonName,
        reasonType, waitingTime != null ? String.valueOf(waitingTime) : null, validateProcess,
        updatedUser, updatedTime
    );
    return dto;
  }

}
