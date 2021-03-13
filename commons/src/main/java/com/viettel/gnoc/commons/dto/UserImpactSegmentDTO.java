package com.viettel.gnoc.commons.dto;


import com.viettel.gnoc.commons.model.UserImpactSegmentEntity;
import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserImpactSegmentDTO extends BaseDto {

  private String id;
  private String userId;
  private String impactSegmentId;
  private String createdTimeFrom;
  private String createdTimeTo;

  private String userName;
  private String role;
  private String array;
  private String unitName;
  private String numCr;

  public UserImpactSegmentDTO(String id, String userId, String impactSegmentId) {
    this.id = id;
    this.userId = userId;
    this.impactSegmentId = impactSegmentId;
  }

  public UserImpactSegmentEntity toEntity() {
    try {
      UserImpactSegmentEntity model = new UserImpactSegmentEntity(
          !StringUtils.validString(id) ? null : Long.valueOf(id),
          !StringUtils.validString(userId) ? null : Long.valueOf(userId),
          !StringUtils.validString(impactSegmentId) ? null : Long.valueOf(impactSegmentId)
      );
      return model;
    } catch (Exception ex) {
      return null;
    }
  }
}
