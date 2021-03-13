package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.maintenance.dto.MrHisDTO;
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
@Table(schema = "OPEN_PM", name = "MR_HIS")
public class MrHisEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_HIS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "MHS_ID", unique = true, nullable = false)
  private Long mhsId;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "UNIT_ID")
  private Long unitId;

  @Column(name = "STATUS")
  private Long status;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CHANGE_DATE")
  private Date changeDate;

  @Column(name = "COMMENTS")
  private String comments;

  @Column(name = "MR_ID", unique = true)
  private Long mrId;

  @Column(name = "NOTES")
  private String notes;

  @Column(name = "ACTION_CODE")
  private Long actionCode;

  public MrHisDTO toDTO() {
    MrHisDTO dto = new MrHisDTO(
        mhsId == null ? null : mhsId.toString(), userId == null ? null : userId.toString(),
        unitId == null ? null : unitId.toString(), status == null ? null : status.toString(),
        changeDate == null ? null
            : DateTimeUtils.convertDateToString(changeDate, "dd/MM/yyyy HH:mm:ss"), comments,
        mrId == null ? null : mrId.toString(), notes,
        actionCode == null ? null : actionCode.toString()
    );
    return dto;
  }
}
