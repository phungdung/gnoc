package com.viettel.gnoc.kedb.dto;

import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ITSOL
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class KedbRatingDTO {

  private String id;
  private String kedbId;
  private String userName;
  private String point;
  private String note;
  private String averagePoint;
  private String numComent;
  private String result;
  private String description;

  public KedbRatingInsideDTO toModelInSide() {
    KedbRatingInsideDTO model = new KedbRatingInsideDTO(
        StringUtils.validString(id) ? Long.valueOf(id) : null,
        StringUtils.validString(kedbId) ? Long.valueOf(kedbId) : null,
        userName,
        StringUtils.validString(point) ? Long.valueOf(point) : null,
        note,
        StringUtils.validString(averagePoint) ? Long.valueOf(averagePoint) : null,
        StringUtils.validString(numComent) ? Long.valueOf(numComent) : null,
        result,
        description
    );
    return model;
  }
}
