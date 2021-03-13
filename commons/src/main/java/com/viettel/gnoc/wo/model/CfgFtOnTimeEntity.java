package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.CfgFtOnTimeDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author namndh
 * @version 1.0
 * @since 19/2/2016
 */
@Entity
@Table(name = "CFG_FT_ON_TIME")
@Getter
@Setter
@NoArgsConstructor
public class CfgFtOnTimeEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CFG_FT_ON_TIME_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "CD_ID")
  private Long cdId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CFG_TIME", nullable = false)
  private Date cfgTime;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "BUSINESS_ID")
  private Long businessId;

  public CfgFtOnTimeEntity(Long id, Long cdId, Date cfgTime,
      Long userId, Long businessId) {
    this.id = id;
    this.cdId = cdId;
    this.cfgTime = cfgTime;
    this.userId = userId;
    this.businessId = businessId;
  }

  public CfgFtOnTimeDTO toDTO() {
    CfgFtOnTimeDTO dto = new CfgFtOnTimeDTO(
        id,
        cdId == null ? null : cdId.toString(),
        cfgTime,
        userId == null ? null : userId.toString(),
        businessId == null ? null : businessId.toString()
    );
    return dto;
  }
}
