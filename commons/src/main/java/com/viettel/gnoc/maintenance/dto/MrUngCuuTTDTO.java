package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrUngCuuTTEntity;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MrUngCuuTTDTO extends BaseDto {

  private String ucttId;
  private String cdId;
  private String title;
  private String description;
  private Date startDate;
  private Date endDate;
  private Date createdDate;
  private String createdUser;
  private Date updatedDate;
  private String updatedUser;
  private String woCode;

  private String cdName;

  private String fileName;
  private String filePath;

  private String startDateFrom;
  private String startDateTo;
  private String endDateFrom;
  private String endDateTo;

  public MrUngCuuTTDTO(String ucttId, String cdId, String title,
      String description, Date startDate, Date endDate, Date createdDate, String createdUser,
      Date updatedDate, String updatedUser, String woCode) {
    this.ucttId = ucttId;
    this.cdId = cdId;
    this.title = title;
    this.description = description;
    this.startDate = startDate;
    this.endDate = endDate;
    this.createdDate = createdDate;
    this.createdUser = createdUser;
    this.updatedDate = updatedDate;
    this.updatedUser = updatedUser;
    this.woCode = woCode;
  }

  public MrUngCuuTTEntity toEntity() {
    try {
      MrUngCuuTTEntity model = new MrUngCuuTTEntity(
          !StringUtils.validString(ucttId) ? null : Long.valueOf(ucttId),
          !StringUtils.validString(cdId) ? null : Long.valueOf(cdId),
          title, description,
          startDate, endDate,
          createdDate, createdUser, updatedDate, updatedUser, woCode
      );
      return model;
    } catch (Exception ex) {
      return null;
    }
  }
}
