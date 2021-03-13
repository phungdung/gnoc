package com.viettel.gnoc.cr.model;


import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.EmployeeDayOffDTO;
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

@Entity
@Table(schema = "COMMON_GNOC", name = "EMPLOYEE_DAYOFF")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDayOffEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "EMPLOYEE_DAYOFF_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID_DAYOFF", nullable = false)
  private Long idDayOff;
  @Column(name = "EMP_ID", nullable = false)
  private Long empId;
  @Column(name = "EMP_UNIT", nullable = false)
  private Long empUnit;
  @Column(name = "EMP_USERNAME", nullable = false)
  private String empUsername;
  @Column(name = "DAYOFF", nullable = false)
  private Date dayOff;
  @Column(name = "VACATION", nullable = false)
  private String vacation;

  public EmployeeDayOffDTO toDTO() {
    return new EmployeeDayOffDTO(
        idDayOff,
        empId,
        empUnit,
        empUsername,
        dayOff,
        vacation
    );
  }
}
