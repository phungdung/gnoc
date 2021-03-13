package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrITSoftCatMarketDTO;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_CAT_MARKET")
public class MrITSoftCatMarketEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_CAT_MARKET_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "MARKET_CODE", nullable = false)
  private String marketCode;
  @Column(name = "MARKET_NAME", nullable = false)
  private String marketName;
  @Column(name = "COUNTRY", nullable = false)
  private String countryCode;

  public MrITSoftCatMarketDTO toDTO() {
    return new MrITSoftCatMarketDTO(
        marketCode, marketName,
        countryCode);
  }
}
