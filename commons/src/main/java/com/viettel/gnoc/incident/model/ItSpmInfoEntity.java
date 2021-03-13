package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.ItSpmInfoDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "ONE_TM", name = "IT_SPM_INFO")
public class ItSpmInfoEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "it_spm_info_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "INCIDENT_ID")
  private Long incidentId;

  @Column(name = "SPM_ID")
  private Long spmId;

  @Column(name = "SPM_CODE")
  private String spmCode;

  @Column(name = "INCIDENT_CODE")
  private String incidentCode;

  public ItSpmInfoEntity(Long id, Long incidentId, Long spmId, String spmCode,
      String incidentCode) {
    this.id = id;
    this.incidentId = incidentId;
    this.spmId = spmId;
    this.spmCode = spmCode;
    this.incidentCode = incidentCode;
  }

  public ItSpmInfoDTO toDTO() {
    ItSpmInfoDTO dto = new ItSpmInfoDTO(
        id,
        incidentId,
        spmId,
        spmCode,
        incidentCode
    );
    return dto;
  }
}
