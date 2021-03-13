package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.maintenance.dto.MrApprovalDepartmentDTO;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_APPROVAL_DEPARTMENT")
public class MrApprovalDepartmentEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_APPROVE_DEPT__SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "MADT_ID", unique = true, nullable = false)
  private Long madtId;

  @Column(name = "MR_ID", unique = true)
  private Long mrId;

  @Column(name = "UNIT_ID")
  private Long unitId;

  @Column(name = "MADT_LEVEL")
  private Long madtLevel;

  @Column(name = "STATUS")
  private Long status;

  @Column(name = "USER_ID")
  private Long userId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "INCOMMING_DATE")
  private Date incommingDate;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "APPROVED_DATE")
  private Date approvedDate;

  @Column(name = "NOTES")
  private String notes;

  @Column(name = "RETURN_CODE")
  private Long returnCode;

  public MrApprovalDepartmentDTO toDTO() {
    MrApprovalDepartmentDTO dto = new MrApprovalDepartmentDTO(
//            madtId==null?null:madtId.toString(),             mrId==null?null:mrId.toString(),             unitId==null?null:unitId.toString(),             madtLevel==null?null:madtLevel.toString(),             status==null?null:status.toString(),             userId==null?null:userId.toString(),             incommingDate==null?null:DateTimeUtils.convertDateToString(incommingDate, ParamUtils.ddMMyyyy),             approvedDate==null?null:DateTimeUtils.convertDateToString(approvedDate, ParamUtils.ddMMyyyy),             notes,             returnCode==null?null:returnCode.toString()
        madtId == null ? null : madtId.toString(), mrId == null ? null : mrId.toString(),
        unitId == null ? null : unitId.toString(), madtLevel == null ? null : madtLevel.toString(),
        status == null ? null : status.toString(), userId == null ? null : userId.toString(),
        incommingDate == null ? null
            : DateTimeUtils.convertDateToString(incommingDate, "dd/MM/yyyy HH:mm:ss"),
        approvedDate == null ? null
            : DateTimeUtils
                .convertDateToString(approvedDate, "dd/MM/yyyy HH:mm:ss"), notes,
        returnCode == null ? null : returnCode.toString()
    );
    return dto;
  }
}
