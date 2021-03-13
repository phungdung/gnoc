package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.TemplateRelationsDTO;
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
@Table(schema = "OPEN_PM", name = "TEMPLATE_RELATIONS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TemplateRelationsEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TEMPLATE_RELATIONS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "TRS_ID  ", nullable = true)
  private Long trsId;
  @Column(name = "TRS_NAME", nullable = true)
  private String trsName;
  @Column(name = "HOST_TEMPLATE_ID", nullable = true)
  private String hostTemplateId;
  @Column(name = "CLIENT_TEMPLATE_ID", nullable = true)
  private String clientTemplateId;
  @Column(name = "RELATION_TYPE", nullable = true)
  private String relationType;

  public TemplateRelationsDTO toDTO() {
    return new TemplateRelationsDTO(
        trsId,
        trsName,
        hostTemplateId,
        clientTemplateId,
        relationType
    );
  }
}
