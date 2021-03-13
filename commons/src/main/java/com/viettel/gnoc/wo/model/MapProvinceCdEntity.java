package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.MapProvinceCdDTO;
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
 * @author itsol
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MAP_PROVICE_CD")
public class MapProvinceCdEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MAP_PROVICE_CD_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;
  @Column(name = "NUMBER_DISTRICT_SC")
  private Long numberDistrictSc;
  @Column(name = "NUMBER_DISTRICT_TK")
  private Long numberDistrictTk;
  @Column(name = "NUMBER_ACCOUNT_TK")
  private Long numberAccountTk;
  @Column(name = "NUMBER_ACCOUNT_SC")
  private Long numberAccountSc;
  @Column(name = "CD_ID")
  private Long cdId;
  @Column(name = "LOCATION_CODE")
  private String locationCode;

  public MapProvinceCdDTO toDto() {
    return new MapProvinceCdDTO(id, numberDistrictSc, numberDistrictTk, numberAccountTk,
        numberAccountSc, cdId, locationCode);
  }


}
