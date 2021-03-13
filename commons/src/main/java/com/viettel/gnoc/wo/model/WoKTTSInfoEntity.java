package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoKTTSInfoDTO;
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
@Table(schema = "WFM", name = "WO_KTTS_INFO")
public class WoKTTSInfoEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_KTTS_INFO_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "WO_ID")
  private Long woId;

  @Column(name = "CONTRACT_ID")
  private Long contractId;

  @Column(name = "CONTRACT_CODE")
  private String contractCode;

  @Column(name = "PROCESS_ACTION_NAME")
  private String processActionName;

  @Column(name = "CONTRACT_PARTNER")
  private String contractPartner;

  @Column(name = "PROCESS_ACTION_ID")
  private Long processActionId;

  @Column(name = "ACTIVE_ENVIRONMENT_ID")
  private Long activeEnvironmentId;

  public WoKTTSInfoDTO toDTO() {
    return new WoKTTSInfoDTO(id, woId, contractId, contractCode, processActionName,
        contractPartner, processActionId, activeEnvironmentId);
  }

}
