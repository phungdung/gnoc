package com.viettel.gnoc.wo.model;


import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.AutoCreateWoOsDTO;
import com.viettel.gnoc.wo.dto.CfgWoHighTempDTO;
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

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "CFG_WO_HIGH_TEMP")
public class CfgWoHighTempEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CFG_WO_HIGH_TEMP_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true)
  private Long id;

  @Column(name = "REASON_LV1_ID")
  private Long reasonLv1Id;

  @Column(name = "REASON_LV2_ID")
  private Long reasonLv2Id;

  @Column(name = "ACTION_ID")
  private Long actionId;

  @Column(name = "ACTION_NAME_ID")
  private String actionNameId;

  @Column(name = "PRIORITY_ID")
  private Long priorityId;

  @Column(name = "CD_TYPE")
  private String cdType;

  @Column(name = "PROCESS_TIME")
  private Long processTime;

  @Column(name = "IS_ASSIGN_FT")
  private Long isAssignFt;

  @Column(name = "REPORT_FAILURE_TYPE")
  private String reportFailureType;

  @Column(name = "STATUS")
  private Long status;

  public CfgWoHighTempDTO toDTO() {
    return new CfgWoHighTempDTO(id, reasonLv1Id, reasonLv2Id, actionId, actionNameId, priorityId,
        cdType, processTime, isAssignFt, reportFailureType, status);
  }
}
