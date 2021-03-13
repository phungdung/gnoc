package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.TempImportDataDTO;
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
@Table(schema = "OPEN_PM", name = "TEMP_IMPORT_DATA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TempImportDataEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TEMP_IMPORT_DATA_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "TIDA_ID", nullable = false)
  private Long tidaId;
  @Column(name = "TEMP_IMPORT_ID", nullable = true)
  private String tempImportId;
  @Column(name = "TEMP_IMPORT_COL_ID", nullable = true)
  private String tempImportColId;
  @Column(name = "TEMP_IMPORT_VALUE", nullable = true)
  private String tempImportValue;
  @Column(name = "CR_ID", nullable = true)
  private String crId;
  @Column(name = "ROW_ORDER", nullable = true)
  private String rowOrder;

  public TempImportDataDTO toDTO() {
    TempImportDataDTO dto = new TempImportDataDTO(
        tidaId,
        tempImportId,
        tempImportColId,
        tempImportValue,
        crId,
        rowOrder
    );
    return dto;
  }
}
