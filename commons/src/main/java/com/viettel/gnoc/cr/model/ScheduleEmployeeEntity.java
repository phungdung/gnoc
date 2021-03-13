package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.ScheduleEmployeeDTO;
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

@Entity
@Table(schema = "COMMON_GNOC", name = "SCHEDULE_EMPLOYEE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleEmployeeEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SCHEDULE_EMPLOYEE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID_EMPLOYEE", nullable = false)
  private Long idEmployee;
  @Column(name = "ID_SCHEDULE", nullable = false)
  private Long idSchedule;
  @Column(name = "NAME_DISPLAY", nullable = false)
  private String nameDisplay;
  @Column(name = "USERNAME", nullable = false)
  private String userName;
  @Column(name = "EMP_ARRAY", nullable = true)
  private String empArray;
  @Column(name = "EMP_CHILDREN", nullable = true)
  private String empChildren;
  @Column(name = "EMP_LEVEL", nullable = true)
  private Long empLevel;
  @Column(name = "UNIT_ID", nullable = false)
  private Long unitId;
  @Column(name = "DAY_OFF", nullable = true)
  private String dayOff;

  public ScheduleEmployeeDTO toDTO() {
    return new ScheduleEmployeeDTO(
        idEmployee,
        idSchedule,
        nameDisplay,
        userName,
        empArray,
        empChildren,
        empLevel,
        unitId,
        dayOff
    );
  }
}
