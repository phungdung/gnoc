package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CrOcsScheduleDTO;
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
 * @author KienPV
 */

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "OPEN_PM", name = "CR_OCS_SCHEDULE")
public class CrOscScheduleEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CR_OCS_SCHEDULE_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CR_OCS_SCHEDULE_ID", unique = true, nullable = false)
  private Long crOscScheduleId;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "CR_PROCESS_ID")
  private Long crProcessId;

  public CrOcsScheduleDTO toDTO() {
    return new CrOcsScheduleDTO(crOscScheduleId, userId, crOscScheduleId);
  }
}
