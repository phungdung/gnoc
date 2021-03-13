package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.sr.model.SRStatusEntity;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.srStatus.null.unique}", clazz = SRStatusEntity.class, uniqueFields = "configGroup,configCode", idField = "configId")
public class SRStatusDTO extends BaseDto {

  //Fields
  private Long configId;
  @NotEmpty(message = "{validation.srStatus.null.configGroup}")
  private String configGroup;
  @NotEmpty(message = "{validation.srStatus.null.configCode}")
  private String configCode;
  @NotEmpty(message = "{validation.srStatus.null.configName}")
  private String configName;
  @NotEmpty(message = "{validation.srStatus.null.status}")
  private String status;
  private String createdUser;
  private Date createdTime;
  private String updatedUser;
  private Date updatedTime;

  public SRStatusEntity toEntity() {
    SRStatusEntity model = new SRStatusEntity(
        configId
        , configGroup
        , configCode
        , configName
        , status
        , createdUser
        , createdTime
        , updatedUser
        , updatedTime
    );
    return model;
  }
}
