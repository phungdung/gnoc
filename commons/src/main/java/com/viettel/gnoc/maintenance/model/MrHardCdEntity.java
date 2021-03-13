package com.viettel.gnoc.maintenance.model;


import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrHardCDDTO;
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
 * @author trungduong
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MR_HARD_CD")
public class MrHardCdEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_HARD_CD_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "HARD_CD_ID", unique = true, nullable = false)
  private Long hardCDId;

  @Column(name = "COUNTRY")
  private String countryCode;

  @Column(name = "CD_ID")
  private Long cdId;

  @Column(name = "STATION_CODE")
  private String stationCode;

  @Column(name = "CREATE_USER")
  private String createUser;

  @Column(name = "REGION")
  private String region;

  public MrHardCDDTO toDTO() {
    MrHardCDDTO dto = new MrHardCDDTO(
        hardCDId
        , countryCode
        , cdId
        , stationCode
        , createUser
        , region
    );
    return dto;
  }
}
