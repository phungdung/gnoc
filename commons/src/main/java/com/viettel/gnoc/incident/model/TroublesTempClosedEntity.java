package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.TroublesTempClosedDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ITSOL
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TROUBLES_TEMP_CLOSED")
public class TroublesTempClosedEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "Troubles_Temp_CLOSED_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")

  //Fields
  @Column(name = "TROUBLES_TEMP_CLOSED_ID", nullable = false)
  private Long troublesTempClosedId;
  @Column(name = "TROUBLE_CODE")
  private String troubleCode;
  @Column(name = "LAST_UPDATE_TIME")
  private Date lastUpdateTime;
  @Column(name = "ASSIGN_TIME_TEMP")
  private Date assignTimeTemp;
  @Column(name = "STATE")
  private Long state;
  @Column(name = "RELATED_KEDB")
  private String relatedKedb;
  @Column(name = "CLOSED_TIME")
  private Date closedTime;
  @Column(name = "CLEAR_TIME")
  private Date clearTime;
  @Column(name = "END_TROUBLE_TIME")
  private Date endTroubleTime;
  @Column(name = "CLOSE_CODE")
  private String closeCode;
  @Column(name = "REASON_ID")
  private Long reasonId;
  @Column(name = "REASON_NAME")
  private String reasonName;
  @Column(name = "ROOT_CAUSE")
  private String rootCause;
  @Column(name = "SOLUTION_TYPE")
  private Long solutionType;
  @Column(name = "WORK_ARROUND")
  private String workArround;
  @Column(name = "CREATED_TIME")
  private Date createdTime;

  public TroublesTempClosedDTO toDTO() {
    return new TroublesTempClosedDTO(troublesTempClosedId, troubleCode, lastUpdateTime,
        assignTimeTemp, state, relatedKedb, closedTime, clearTime, endTroubleTime, closeCode,
        reasonId, reasonName, rootCause, solutionType, workArround, createdTime);
  }

}
