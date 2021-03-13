package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoMerchandiseInsideDTO;
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
@Table(schema = "WFM", name = "WO_MERCHANDISE")
public class WoMerchandiseEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_MERCHANDISE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "WO_ID")
  private Long woId;

  @Column(name = "MERCHANDISE_CODE")
  private String merchandiseCode;

  @Column(name = "SERIAL")
  private String serial;

  @Column(name = "QUANTITY")
  private Double quantity;

  @Column(name = "MERCHANDISE_NAME")
  private String merchandiseName;

  @Column(name = "CAUSE")
  private Long cause;

  @Column(name = "LONG_TERM_ASSET_ID")
  private Long longTermAssetId;

  public WoMerchandiseInsideDTO toDTO() {
    return new WoMerchandiseInsideDTO(id, woId, merchandiseCode, serial, quantity, merchandiseName,
        cause, longTermAssetId, null, null, null, null, null, null, null, null, null);
  }

}
