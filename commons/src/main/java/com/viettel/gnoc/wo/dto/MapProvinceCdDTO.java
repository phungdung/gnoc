package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.wo.model.MapProvinceCdEntity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ITSOL
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Unique(message = "{validation.mapProvinceCd.null.unique}", clazz = MapProvinceCdEntity.class, uniqueField = "locationCode", idField = "id")
public class MapProvinceCdDTO extends BaseDto {

  private Long id;
  @NotNull(message = "{validation.mapProvinceCd.null.numberDistrictSc}")
  private Long numberDistrictSc;

  @NotNull(message = "{validation.mapProvinceCd.null.numberDistrictTk}")
  private Long numberDistrictTk;

  @NotNull(message = "{validation.mapProvinceCd.null.numberAccountTk}")
  private Long numberAccountTk;

  @NotNull(message = "{validation.mapProvinceCd.null.numberAccountSc}")
  private Long numberAccountSc;

  @NotNull(message = "{validation.mapProvinceCd.null.cdId}")
  private Long cdId;

  @NotEmpty(message = "{validation.mapProvinceCd.null.locationCode}")
  private String locationCode;
  private String cdName;
  private String locationName;
  private String resuilt;
  private String numberDistrictScExp;
  private String numberDistrictTkExp;
  private String numberAccountTkExp;
  private String numberAccountScExp;

  public MapProvinceCdEntity toEntity() {
    return new MapProvinceCdEntity(id, numberDistrictSc, numberDistrictTk, numberAccountTk,
        numberAccountSc, cdId, locationCode);
  }

  public MapProvinceCdDTO(Long id, Long numberDistrictSc, Long numberDistrictTk,
      Long numberAccountTk, Long numberAccountSc, Long cdId, String locationCode) {
    this.id = id;
    this.numberDistrictSc = numberDistrictSc;
    this.numberDistrictTk = numberDistrictTk;
    this.numberAccountTk = numberAccountTk;
    this.numberAccountSc = numberAccountSc;
    this.cdId = cdId;
    this.locationCode = locationCode;
  }
}
