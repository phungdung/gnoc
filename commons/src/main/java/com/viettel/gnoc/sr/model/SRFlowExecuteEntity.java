
package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRFlowExecuteDTO;
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
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "OPEN_PM", name = "SR_FLOW_EXECUTE")
public class SRFlowExecuteEntity {

  static final long serialVersionUID = 1L;

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_FLOW_EXECUTE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "FLOW_ID", nullable = false)
  private Long flowId;

  @Column(name = "FLOW_NAME")
  private String flowName;

  @Column(name = "FLOW_DESCRIPTION")
  private String flowDescription;

  @Column(name = "COUNTRY")
  private Long country;

  public SRFlowExecuteDTO toDTO() {
    SRFlowExecuteDTO dto = new SRFlowExecuteDTO(
        flowId
        , flowName
        , flowDescription
        , country
    );
    return dto;
  }
}

