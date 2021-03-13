package com.viettel.gnoc.wo.model;


import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.UserGroupCategoryDTO;
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
@Table(schema = "OPEN_PM", name = "USER_GROUP_CATEGORY")
public class UserGroupCategoryEntity {

  static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "USER_GROUP_CATEGORY_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "UGCY_ID", nullable = false)
  private Long ugcyId;

  @Column(name = "UGCY_CODE", nullable = false)
  private String ugcyCode;

  @Column(name = "UGCY_NAME", nullable = false)
  private String ugcyName;

  @Column(name = "UGCY_SYSTEM", nullable = false)
  private Long ugcySystem;

  @Column(name = "UGCY_IS_ACTIVE", nullable = false)
  private Long ugcyIsActive;

  public UserGroupCategoryDTO toDTO() {
    UserGroupCategoryDTO dto = new UserGroupCategoryDTO(
        ugcyId,
        ugcyCode,
        ugcyName,
        ugcySystem,
        ugcyIsActive
    );
    return dto;
  }
}
