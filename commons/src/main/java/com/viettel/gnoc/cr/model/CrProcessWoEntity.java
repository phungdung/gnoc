package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CrProcessWoDTO;
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
@Table(schema = "OPEN_PM", name = "CR_PROCESS_WO")
public class CrProcessWoEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CR_PROCESS_WO_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CR_PROCESS_WO_ID", unique = true, nullable = false)
  private Long crProcessWoId;

  @Column(name = "WO_NAME")
  private String woName;

  @Column(name = "WO_STATUS")
  private Long woStatus;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "IS_REQUIRE")
  private Long isRequire;

  @Column(name = "DURATION_WO")
  private Double durationWo;

  @Column(name = "CR_PROCESS_ID")
  private Long crProcessId;

  @Column(name = "IS_REQUIRE_WO")
  private Long isRequireWo;

  @Column(name = "WO_TYPE_ID")
  private Long woTypeId;

  @Column(name = "CREATE_WHEN_CLOSE_CR")
  private Long createWhenCloseCr;

  public CrProcessWoDTO toDTO() {
    return new CrProcessWoDTO(crProcessWoId, woName, woStatus,
        description, isRequire, isRequireWo, durationWo,
        crProcessId, woTypeId, createWhenCloseCr);
  }
}
