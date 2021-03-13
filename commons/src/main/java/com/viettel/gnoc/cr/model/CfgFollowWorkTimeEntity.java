package com.viettel.gnoc.cr.model;


import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CfgFollowWorkTimeDTO;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "CONFIG_FOLLOW_WORKTIME")
public class CfgFollowWorkTimeEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OPEN_PM.CONFIG_FOLLOW_WORKTIME_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CONFIG_FOLLOW_WORKTIME_ID", unique = true, nullable = false)
  private Long configFollowWorkTimeId;

  @Column(name = "SYSTEM")
  private String system;

  @Column(name = "CATEGORY_ID")
  private Long categoryId;

  @Column(name = "CAT_ITEM_ID")
  private Long catItemId;

  @Column(name = "STEP_TIME")
  private Long stepTime;

  @Column(name = "CATEGORY_NAME")
  private String categoryName;

  @Column(name = "CAT_ITEM_NAME")
  private String catItemName;

  @Column(name = "CR_TYPE")
  private Long crType;

  @Column(name = "TT_ACTION_CLASS")
  private Long ttActionClass;

  public CfgFollowWorkTimeDTO toDTO() {
    CfgFollowWorkTimeDTO dto = new CfgFollowWorkTimeDTO(
        configFollowWorkTimeId,
        system,
        categoryId,
        catItemId,
        stepTime,
        categoryName,
        catItemName,
        crType,
        ttActionClass
    );
    return dto;
  }

}
