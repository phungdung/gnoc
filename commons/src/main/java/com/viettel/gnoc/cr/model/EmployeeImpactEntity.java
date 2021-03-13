package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.EmployeeImpactDTO;
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
@Table(schema = "COMMON_GNOC", name = "EMPLOYEE_IMPACT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeImpactEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "EMPLOYEE_IMPACT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID_IMPACT", nullable = false)
  private Long idImpact;
  @Column(name = "EMP_ID", nullable = false)
  private Long empId;
  @Column(name = "EMP_USERNAME", nullable = false)
  private String empUsername;
  @Column(name = "EMP_ARRAY", nullable = false)
  private Long empArray;
  @Column(name = "EMP_LEVEL", nullable = false)
  private Long empLevel;
  @Column(name = "UPDATED_USER", nullable = true)
  private String updatedUser;
  @Column(name = "UPDATED_TIME", nullable = true)
  private Date updatedTime;
  @Column(name = "EMP_ARRAY_CHILD", nullable = true)
  private String empArrayChild;


  public EmployeeImpactDTO toDTO() {
    return new EmployeeImpactDTO(
        idImpact,
        empId,
        empUsername,
        empArray,
        empLevel,
        updatedUser,
        updatedTime,
        empArrayChild
    );
  }
}
