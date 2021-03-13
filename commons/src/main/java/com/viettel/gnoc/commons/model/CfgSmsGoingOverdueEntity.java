package com.viettel.gnoc.commons.model;

import com.viettel.gnoc.commons.dto.CfgSmsGoingOverdueDTO;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "COMMON_GNOC", name = "CFG_SMS_GOING_OVERDUE")
public class CfgSmsGoingOverdueEntity implements Serializable {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CFG_SMS_GOING_OVERDUE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
  @Column(name = "CFG_ID")
  private Long cfgId;

  @Column(name = "CFG_NAME", nullable = false)
  private String cfgName;

  @Column(name = "UNIT_ID", nullable = false)
  private Long unitId;

  @Column(name = "UNIT_NAME")
  private String unitName;

  @Column(name = "LOCATION_ID")
  private Long locationId;

  @Column(name = "LOCATION_NAME")
  private String locationName;

  @Column(name = "PRIORITY_ID")
  private Long priorityId;

  @Column(name = "PRIORITY_NAME")
  private String priorityName;

  @Column(name = "LEVEL_ID")
  private Long levelId;

  @Column(name = "TIME_PROCESS")
  private Double timeProcess;

  public CfgSmsGoingOverdueDTO toDTO() {
    CfgSmsGoingOverdueDTO dto = new CfgSmsGoingOverdueDTO(
        cfgId == null ? null : cfgId.toString(),
        cfgName,
        unitId == null ? null : unitId.toString(),
        unitName,
        locationId == null ? null : locationId.toString(),
        locationName,
        priorityId == null ? null : priorityId.toString(),
        priorityName,
        levelId == null ? null : levelId.toString(),
        timeProcess == null ? null : timeProcess.toString()
    );
    return dto;
  }
}
