package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.DashboardDTO;
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
@Table(schema = "WFM", name = "DASHBOARD")
public class DashboardEntity {

  static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "DASHBOARD", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true)
  private Long id;

  @Column(name = "DASHBOARD_CODE")
  private String dashboardCode;

  @Column(name = "COLUMN1")
  private String column1;

  @Column(name = "COLUMN2")
  private String column2;

  @Column(name = "COLUMN3")
  private String column3;

  @Column(name = "VALUE1")
  private String value1;

  @Column(name = "VALUE2")
  private String value2;

  @Column(name = "VALUE3")
  private String value3;

  @Column(name = "DATE_TIME")
  private Date dateTime;

  public DashboardDTO toDTO() {
    return new DashboardDTO(id, dashboardCode, column1, column2, column3, value1, value2,
        value3, dateTime);
  }
}
