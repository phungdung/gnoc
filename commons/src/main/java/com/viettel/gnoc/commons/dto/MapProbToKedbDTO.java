package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.MapProbToKedbEntity;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MultiFieldUnique(message = "{validation.mapProbTokedb.null.unique}", clazz = MapProbToKedbEntity.class, uniqueFields = "probTypeIdLv1,probTypeIdLv2,probTypeIdLv3,kedbCode", idField = "id")
public class MapProbToKedbDTO extends BaseDto {

  private Long id;
  private Long probTypeIdLv1;
  private String probTypeNameLv1;
  private Long probTypeIdLv2;
  private String probTypeNameLv2;
  private Long probTypeIdLv3;
  private String probTypeNameLv3;
  private String kedbCode;
  private String resultImport;

  public MapProbToKedbDTO(Long id, Long probTypeIdLv1, String probTypeNameLv1,
      Long probTypeIdLv2, String probTypeNameLv2, Long probTypeIdLv3, String probTypeNameLv3,
      String kedbCode) {
    this.id = id;
    this.probTypeIdLv1 = probTypeIdLv1;
    this.probTypeNameLv1 = probTypeNameLv1;
    this.probTypeIdLv2 = probTypeIdLv2;
    this.probTypeNameLv2 = probTypeNameLv2;
    this.probTypeIdLv3 = probTypeIdLv3;
    this.probTypeNameLv3 = probTypeNameLv3;
    this.kedbCode = kedbCode;
  }

  public MapProbToKedbEntity toEntity() {
    return new MapProbToKedbEntity(id,
        probTypeIdLv1, probTypeNameLv1,
        probTypeIdLv2, probTypeNameLv2,
        probTypeIdLv3, probTypeNameLv3,
        kedbCode);
  }

}
