package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
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

@Entity
@Table(schema = "ONE_TM", name = "CFG_MAP_NET_LEVEL_INC_TYPE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CfgMapNetLevelIncTypeEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "ONE_TM.cfg_map_net_level_inc_type_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "TROUBLE_TYPE_ID")
  private Long troubleTypeId;

  @Column(name = "NETWORK_LEVEL_ID")
  private String networkLevelId;

  @Column(name = "TROUBLE_TYPE_NAME")
  private String troubleTypeName;

  @Column(name = "NETWORK_LEVEL_NAME")
  private String networkLevelName;

  public CfgMapNetLevelIncTypeDTO toDTO() {
    CfgMapNetLevelIncTypeDTO dto = new CfgMapNetLevelIncTypeDTO(
        id, troubleTypeId,
        networkLevelId, networkLevelName, troubleTypeName
    );
    return dto;
  }
}
