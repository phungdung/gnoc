package com.viettel.gnoc.wo.model;

import com.viettel.gnoc.wo.dto.CfgWoHelpVsmartDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(schema = "WFM", name = "CFG_WO_HELP_VSMART")
public class CfgWoHelpVsmartEntity {

  @Id
  @Column(name = "ID", nullable = false)
  private Long id;
  @Column(name = "SYSTEM_ID")
  private Long systemId;
  @Column(name = "TYPE_ID")
  private String typeId;
  @Column(name = "FILE_ID")
  private String fileId;
  @Column(name = "TYPE_NAME")
  private String typeName;
  @Column(name = "SYNC_STATUS")
  private Long syncStatus;

  public CfgWoHelpVsmartDTO toDTO() {
    CfgWoHelpVsmartDTO dto = new CfgWoHelpVsmartDTO(id, systemId, typeId, fileId, typeName);
    return dto;
  }
}
