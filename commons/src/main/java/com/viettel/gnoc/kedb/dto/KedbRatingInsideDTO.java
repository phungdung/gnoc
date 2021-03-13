package com.viettel.gnoc.kedb.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.kedb.model.KedbRatingEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class KedbRatingInsideDTO extends BaseDto {

  private Long id;
  private Long kedbId;
  private String userName;
  private Long point;
  private String note;
  private Long averagePoint;
  private Long numComent;
  private String result;
  private String description;

  public KedbRatingInsideDTO(Long id, Long kedbId, String userName, Long point, String note) {
    this.id = id;
    this.kedbId = kedbId;
    this.userName = userName;
    this.point = point;
    this.note = note;
  }

  public KedbRatingEntity toEntity() {
    return new KedbRatingEntity(id, kedbId, userName, point, note);
  }

  public KedbRatingDTO toModelOutsideSide() {
    KedbRatingDTO model = new KedbRatingDTO(
        StringUtils.validString(id) ? String.valueOf(id) : null,
        StringUtils.validString(kedbId) ? String.valueOf(kedbId) : null,
        userName,
        StringUtils.validString(point) ? String.valueOf(point) : null,
        note,
        StringUtils.validString(averagePoint) ? String.valueOf(averagePoint) : null,
        StringUtils.validString(numComent) ? String.valueOf(numComent) : null,
        result,
        description
    );
    return model;
  }
}
