package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.CfgTroubleCallIpccDTO;
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
@Table(schema = "COMMON_GNOC", name = "CFG_TROUBLE_CALL_IPCC")
public class CfgTroubleCallIpccEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CFG_TROUBLE_CALL_IPCC_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = false)
  private Long id;
  @Column(name = "CFG_ID", nullable = false)
  private Long cfgId;
  @Column(name = "LEVEL_CALL")
  private Long levelCall;
  @Column(name = "TIME_PROCESS")
  private Long timeProcess;
  @Column(name = "FILE_NAME")
  private String fileName;
  @Column(name = "receive_user_name")
  private String receiveUserName;

  public CfgTroubleCallIpccDTO toDTO() {
    return new CfgTroubleCallIpccDTO(id, cfgId, levelCall, timeProcess, fileName,
        receiveUserName);
  }
}
