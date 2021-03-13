package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.CrDtTemplateFileDTO;
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


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "CR_DT_TEMPLATE_FILE")
public class CrDtTemplateFileEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OPEN_PM.CR_DT_TEMPLATE_FILE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CR_DT_TEMPLATE_FILE_ID", unique = true, nullable = false)
  private Long crDtTemplateFileId;
  @Column(name = "CR_PROCESS_ID", nullable = false)
  private Long crProcessId;
  @Column(name = "FILE_NAME", nullable = false)
  private String fileName;
  @Column(name = "TEMPLATE_TYPE", nullable = false)
  private String templateType;
  @Column(name = "MODIFIED_DATE", unique = true, nullable = false)
  private Date modifiedDate;

  public CrDtTemplateFileDTO toDTO() {
    return new CrDtTemplateFileDTO(crDtTemplateFileId, crProcessId, fileName, templateType,
        modifiedDate);
  }
}
