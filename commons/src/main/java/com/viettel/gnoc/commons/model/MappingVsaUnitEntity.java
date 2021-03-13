package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.MappingVsaUnitDTO;
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
 * @author sondt20
 * @version 1.0
 * @since 19/02/2016 11:48:33
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "MAPPING_VSA_UNIT")
public class MappingVsaUnitEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "mapping_vsa_unit_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "mvutId", unique = true, nullable = false)
  private Long mvutId;

  @Column(name = "VSA_UNIT_ID")
  private Long vsaUnitId;

  @Column(name = "APP_UNIT_ID")
  private Long appUnitId;

  public MappingVsaUnitDTO toDTO() {
    MappingVsaUnitDTO dto = new MappingVsaUnitDTO(
        mvutId,
        vsaUnitId,
        appUnitId
    );
    return dto;
  }
}
