package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.ShiftItDTO;
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
@Table(schema = "COMMON_GNOC", name = "SHIFT_IT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftItEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SHIFT_IT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = false)
  private Long id;
  @Column(name = "KPI")
  private Long kpi;
  @Column(name = "EMERGENCY")
  private Double emergency;
  @Column(name = "SERIOUS")
  private Double serious;
  @Column(name = "MEDIUM")
  private Double medium;
  @Column(name = "TOTAL")
  private Double total;
  @Column(name = "NOTE")
  private String note;
  @Column(name = "COUNTRY")
  private Long country;
  @Column(name = "SHIFT_HANDOVER_ID", nullable = false)
  private Long shiftHandoverId;
  @Column(name = "TYPE_INCIDENT")
  private Long typeIncident;

  public ShiftItDTO toDTO() {
    return new ShiftItDTO(
        id,
        kpi,
        emergency,
        serious,
        medium,
        total,
        note,
        country,
        shiftHandoverId,
        typeIncident
    );
  }
}
