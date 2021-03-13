package com.viettel.gnoc.od.model;

import com.viettel.gnoc.od.dto.OdCfgScheduleCreateDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "OD_CFG_SCHEDULE_CREATE")
public class OdCfgScheduleCreateEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OD_CFG_SCHEDULE_CREATE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
  @Column(name = "CFG_ID", unique = true)
  private Long id;

  @Column(name = "OD_NAME")
  @Nationalized
  private String odName;

  @Column(name = "OD_DESCRIPTION")
  @Nationalized
  private String odDescription;

  @Column(name = "OD_TYPE_ID")
  private Long odTypeId;

  @Column(name = "OD_PRIORITY")
  private Long odPriority;

  @Column(name = "SCHEDULE")
  private Long schedule;

  @Column(name = "RECEIVE_UNIT")
  private String receiveUnit;

  @Column(name = "OD_FILE_ID")
  private String odFileId;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_TIME")
  private Date lastUpdateTime;

  public OdCfgScheduleCreateDTO toDTO() {
    return new OdCfgScheduleCreateDTO(id, odName, odDescription, odTypeId, odPriority, schedule,
        receiveUnit, odFileId, lastUpdateTime);
  }
}
