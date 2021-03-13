package com.viettel.gnoc.od.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.od.dto.OdTypeMapLocationDTO;
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
@Table(schema = "WFM", name = "OD_TYPE_MAP_LOCATION")
public class OdTypeMapLocationEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OD_TYPE_MAP_LOCATION_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true)
  private Long id;

  @Column(name = "OD_TYPE_ID")
  private Long odTypeId;

  @Column(name = "LOCATION_CODE")
  private String locationCode;

  @Column(name = "CREATE_UNIT_ID")
  private Long createUnitId;

  @Column(name = "RECEIVE_UNIT_ID")
  private Long receiveUnitId;

  public OdTypeMapLocationDTO toDTO() {
    return new OdTypeMapLocationDTO(id, odTypeId, locationCode, createUnitId, receiveUnitId);
  }

}
