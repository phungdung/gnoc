package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrCfgMarketDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_CFG_MARKET")
public class MrCfgMarketEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_CFG_MARKET_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID_MARKET", unique = true, nullable = false)
  private Long idMarket;

  @Column(name = "MARKET_CODE")
  private Long marketCode;

  @Column(name = "CREATED_USER_SOFT")
  private Long createdUserSoft;

  @Column(name = "UPDATED_USER")
  private String updatedUser;

  @Column(name = "UPDATED_TIME")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date updatedTime;

  @Column(name = "MARKET_NAME")
  private String marketName;

  @Column(name = "CREATED_USER_HARD")
  private Long createdUserHard;

  @Column(name = "CREATED_USER_IT_SOFT")
  private Long createdUserItSoft;

  @Column(name = "CREATED_USER_IT_HARD")
  private Long createdUserItHard;


  public MrCfgMarketDTO toDTO() {
    return new MrCfgMarketDTO(
        idMarket,
        marketCode,
        createdUserSoft,
        updatedUser,
        updatedTime,
        marketName,
        createdUserHard,
        createdUserItSoft,
        createdUserItHard
    );
  }
}
