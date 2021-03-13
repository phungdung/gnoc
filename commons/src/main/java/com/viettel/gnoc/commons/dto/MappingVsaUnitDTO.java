package com.viettel.gnoc.commons.dto;


import com.viettel.gnoc.commons.model.MappingVsaUnitEntity;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author sondt20
 * @version 1.0
 * @since 19/02/2016 11:48:33
 */
@Getter
@Setter
@NoArgsConstructor
//@MultiFieldUnique(message = "{validation.MappingVsaUnit.null.unique}", clazz = MappingVsaUnitEntity.class, uniqueFields = "appUnitId,vsaUnitId", idField = "mvutId")
public class MappingVsaUnitDTO extends BaseDto {

  private Long mvutId;
  //  @NotNull(message = "validation.MappingVsaUnitDTO.vsaUnitId.NotNull")
  private Long vsaUnitId;
  @NotNull(message = "validation.MappingVsaUnitDTO.appUnitId.NotNull")
  private Long appUnitId;
  private String appUnitCode;
  private String vsaUnitCode;
  private String appUnitName;
  private String vsaUnitName;
  private String appUnitNameFull;
  private String vsaUnitNameFull;
  private Long defaultSortField;
  private String resultImport;
  private List<Long> vsaUnit;

  public MappingVsaUnitEntity toEntity() {
    return new MappingVsaUnitEntity(mvutId, vsaUnitId, appUnitId);
  }

  public MappingVsaUnitDTO(Long mvutId, Long vsaUnitId, Long appUnitId) {
    this.mvutId = mvutId;
    this.vsaUnitId = vsaUnitId;
    this.appUnitId = appUnitId;
  }
}
