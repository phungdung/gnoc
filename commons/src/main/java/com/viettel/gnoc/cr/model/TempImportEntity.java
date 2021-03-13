package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.TempImportDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "OPEN_PM", name = "TEMP_IMPORT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TempImportEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TEMP_IMPORT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "TEMP_IMPORT_ID", nullable = false)
  private Long tempImportId;
  @Column(name = "CODE", nullable = true)
  private String code;
  @Column(name = "NAME", nullable = true)
  private String name;
  @Column(name = "TOTAL_COLUMN", nullable = true)
  private Long totalColumn;
  @Column(name = "TITLE", nullable = true)
  private String title;
  @Column(name = "CREATER_ID", nullable = true)
  private Long createrId;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATER_TIME", nullable = true)
  private Date createrTime;
  @Column(name = "PROCESS_TYPE_ID", nullable = true)
  private Long processTypeId;
  @Column(name = "IS_ACTIVE", nullable = true)
  private Long isActive;
  @Column(name = "WEBSERVICE_METHOD_ID", nullable = true)
  private Long webServiceMethodId;
  @Column(name = "IS_VALIDATE_INPUT", nullable = true)
  private Long isValidateInput;
  @Column(name = "IS_VALIDATE_OUTPUT", nullable = true)
  private Long isValidateOutput;
  @Column(name = "IS_REVERT", nullable = true)
  private Long isRevert;
  @Column(name = "IS_MEC_FILE", nullable = true)
  private Long isMecFile;
  @Column(name = "PATH", nullable = true)
  private String path;
  @Column(name = "APPLIED_SYSTEM", nullable = true)
  private Long appliedSystem;
  @Column(name = "IS_EDITABLE", nullable = true)
  private Long isEditable;

  public TempImportDTO toDTO() {
    TempImportDTO dto = new TempImportDTO(
        tempImportId,
        code,
        name,
        totalColumn,
        title, createrId,
        createrTime,
        processTypeId,
        isActive,
        webServiceMethodId,
        isValidateInput,
        isValidateOutput,
        isRevert,
        isMecFile,
        path,
        appliedSystem,
        isEditable
    );
    return dto;
  }
}
