package com.viettel.gnoc.cr.dto;


import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.cr.model.CfgFollowWorkTimeEntity;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MultiFieldUnique(message = "{validation.CfgFollowWorkTimeDTO.null.unique}", clazz = CfgFollowWorkTimeEntity.class, uniqueFields = "system,categoryId,catItemId", idField = "configFollowWorkTimeId")
public class CfgFollowWorkTimeDTO extends BaseDto {

  private Long configFollowWorkTimeId;
  @NotEmpty(message = "{validation.CfgFollowWorkTimeDTO.null.system}")
  private String system;
  private Long categoryId;
  private Long catItemId;
  //  @NotNull(message = "{validation.CfgFollowWorkTimeDTO.null.stepTime}")
  private Long stepTime;
  //  @NotEmpty(message = "{validation.CfgFollowWorkTimeDTO.null.categoryName}")
  private String categoryName;
  //  @NotEmpty(message = "{validation.CfgFollowWorkTimeDTO.null.catItemName}")
  private String catItemName;
  //  @NotNull(message = "{validation.CfgFollowWorkTimeDTO.null.crType}")
  private Long crType;
  //  @NotNull(message = "{validation.CfgFollowWorkTimeDTO.null.ttActionClass}")
  private Long ttActionClass;

  private String systemOLD;

  public CfgFollowWorkTimeDTO(Long configFollowWorkTimeId, String system,
      Long categoryId, Long catItemId, Long stepTime, String categoryName,
      String catItemName, Long crType, Long ttActionClass) {
    this.configFollowWorkTimeId = configFollowWorkTimeId;
    this.system = system;
    this.categoryId = categoryId;
    this.catItemId = catItemId;
    this.stepTime = stepTime;
    this.categoryName = categoryName;
    this.catItemName = catItemName;
    this.crType = crType;
    this.ttActionClass = ttActionClass;
  }

  public CfgFollowWorkTimeEntity toEntity() {
    CfgFollowWorkTimeEntity model = new CfgFollowWorkTimeEntity(
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
    return model;
  }
}
