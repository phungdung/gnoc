package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.WoTestServiceConfEntity;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.woTestServiceConf.null.unique}", clazz = WoTestServiceConfEntity.class, uniqueFields = "name", idField = "id")
public class WoTestServiceConfDTO extends BaseDto {

  private Long id;
  @NotEmpty(message = "{validation.woTestServiceConf.null.name}")
  private String name;
  @NotEmpty(message = "{validation.woTestServiceConf.null.woContent}")
  private String woContent;
  @NotNull(message = "{validation.woTestServiceConf.null.woType}")
  private Long woType;
  @NotNull(message = "{validation.woTestServiceConf.null.woParentType}")
  private Long woParentType;
  @NotNull(message = "{validation.woTestServiceConf.null.woPriority}")
  private Long woPriority;
  @NotNull(message = "{validation.woTestServiceConf.null.fileId}")
  private Long fileId;
  private Long cdId;
  @NotNull(message = "{validation.woTestServiceConf.null.deltaTime1}")
  private Double deltaTime1;
  @NotNull(message = "{validation.woTestServiceConf.null.deltaTime2}")
  private Double deltaTime2;

  private String woTypeName;
  private String woParentTypeName;
  private String woPriorityName;
  private String cdName;

  public WoTestServiceConfDTO(Long id, String name, String woContent, Long woType, Long woPriority,
      Long fileId,
      Long cdId, Double deltaTime1, Double deltaTime2, Long woParentType) {
    this.id = id;
    this.name = name;
    this.woContent = woContent;
    this.woType = woType;
    this.woPriority = woPriority;
    this.fileId = fileId;
    this.cdId = cdId;
    this.deltaTime1 = deltaTime1;
    this.deltaTime2 = deltaTime2;
    this.woParentType = woParentType;
  }

  public WoTestServiceConfEntity toEntity() {
    return new WoTestServiceConfEntity(id, name, woContent, woType, woPriority, fileId, cdId,
        deltaTime1,
        deltaTime2, woParentType);
  }
}
