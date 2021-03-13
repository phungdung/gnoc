package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.TempImportColDTO;
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
@Table(schema = "OPEN_PM", name = "TEMP_IMPORT_COL")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TempImportColEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TEMP_IMPORT_COL_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "TEMP_IMPORT_COL_ID", nullable = false)
  private Long tempImportColId;
  @Column(name = "CODE", nullable = true)
  private String code;
  @Column(name = "TITLE", nullable = true)
  private String title;
  @Column(name = "IS_MERGE", nullable = true)
  private Long isMerge;
  @Column(name = "COL_POSITION", nullable = true)
  private Long colPosition;
  @Column(name = "TEMP_IMPORT_ID", nullable = true)
  private Long tempImportId;
  @Column(name = "METHOD_PARAMETER_ID", nullable = true)
  private Long methodParameterId;
  @Column(name = "POS_BK", nullable = true)
  private Long posBk;

  public TempImportColDTO toDTO() {
    return new TempImportColDTO(
        tempImportColId,
        code,
        title,
        isMerge,
        colPosition,
        tempImportId,
        methodParameterId,
        posBk
    );
  }
}
