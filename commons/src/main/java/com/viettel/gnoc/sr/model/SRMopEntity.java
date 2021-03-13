package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRMopDTO;
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
@Table(schema = "OPEN_PM", name = "SR_MOP")
public class SRMopEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_MOP_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID")
  private Long id;

  @Column(name = "SR_ID")
  private Long srId;

  @Column(name = "DT_ID")
  private Long dtId;

  @Column(name = "DT_NAME")
  private String dtName;

  @Column(name = "TEMPLATE_ID")
  private Long templateId;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "IP_NODE")
  private String ipNode;

  @Column(name = "PROCESS_ID")
  private String processId;


  public SRMopDTO toDTO() {
    SRMopDTO dto = new SRMopDTO(
        id == null ? null : id.toString(),
        srId == null ? null : srId.toString(),
        dtId == null ? null : dtId.toString(),
        dtName,
        templateId == null ? null : templateId.toString(),
        type, ipNode, processId);
    return dto;
  }
}
