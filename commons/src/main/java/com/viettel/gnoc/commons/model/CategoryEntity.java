package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.CategoryDTO;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "CATEGORY")
public class CategoryEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CATEGORY_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CATEGORY_ID", nullable = false)
  private Long categoryId;

  @Column(name = "CATEGORY_NAME", nullable = false)
  private String categoryName;

  @Column(name = "CATEGORY_CODE", unique = true)
  private String categoryCode;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "EDITABLE")
  private Long editable;

  @Column(name = "PARENT_CATEGORY_ID")
  private Long parentCategoryId;

  public CategoryDTO toDTO() {
    CategoryDTO dto = new CategoryDTO(
        categoryId
        , categoryName
        , categoryCode
        , description
        , editable
        , parentCategoryId
    );
    return dto;
  }
}
