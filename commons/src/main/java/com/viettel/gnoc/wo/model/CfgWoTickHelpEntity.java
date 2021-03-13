package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.CfgWoTickHelpInsideDTO;
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
@Table(name = "CFG_WO_TICK_HELP")
public class CfgWoTickHelpEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "cfg_wo_tick_help_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = false)
  private Long id;
  @Column(name = "WO_ID")
  private Long woId;
  @Column(name = "SYSTEM_NAME")
  private String systemName;
  @Column(name = "TICK_HELP_ID")
  private Long tickHelpId;
  @Column(name = "STATUS")
  private Long status;

  public CfgWoTickHelpInsideDTO toDTO() {
    return new CfgWoTickHelpInsideDTO(id, woId, systemName, tickHelpId, status);
  }


}
