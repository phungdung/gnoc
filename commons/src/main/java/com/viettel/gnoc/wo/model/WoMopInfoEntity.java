package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoMopInfoDTO;
import java.util.Date;
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
 * @author ITSOL
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "WO_MOP_INFO")
public class WoMopInfoEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "wo_mop_info_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WO_MOP_ID", nullable = false)
  private Long woMopId;
  @Column(name = "WO_ID")
  private Long woId;
  @Column(name = "MOP_ID")
  private Long mopId;
  @Column(name = "MOP_CODE")
  private String mopCode;
  @Column(name = "RUN_TIME")
  private Date runTime;
  @Column(name = "RESPONSE_TIME")
  private Date responseTime;
  @Column(name = "RESULT")
  private String result;

  public WoMopInfoDTO toDto() {
    return new WoMopInfoDTO(woMopId, woId, mopId, mopCode, runTime, responseTime, result);
  }


}
