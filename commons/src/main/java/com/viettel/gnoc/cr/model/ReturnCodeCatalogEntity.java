package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.ReturnCodeCatalogDTO;
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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "OPEN_PM", name = "RETURN_CODE_CATALOG")
public class ReturnCodeCatalogEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "RETURN_CODE_CATALOG_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "RCCG_ID", nullable = false)
  private Long returnCodeCategoryId;
  @Column(name = "RETURN_CODE")
  private String returnCode;
  @Column(name = "RETURN_TITLE")
  private String returnTitle;
  @Column(name = "RETURN_CATEGORY")
  private Long returnCategory;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "IS_ACTIVE")
  private Long isActive;
  @Column(name = "IS_EDITABLE")
  private Long isEditable;

  public ReturnCodeCatalogDTO toDTO() {
    return new ReturnCodeCatalogDTO(
        returnCodeCategoryId,
        returnCode,
        returnTitle,
        returnCategory,
        description,
        isActive,
        isEditable
    );
  }
}
