package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.DashboardEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO extends BaseDto {

  private Long id;
  private String dashboardCode;
  private String column1;
  private String column2;
  private String column3;
  private String value1;
  private String value2;
  private String value3;
  private Date dateTime;

  private String time_start;
  private String time_end;

  public DashboardDTO(Long id, String dashboardCode, String column1, String column2, String column3, String value1,
                      String value2, String value3, Date dateTime) {
        this.id = id;
        this.dashboardCode = dashboardCode;
        this.column1 = column1;
        this.column2 = column2;
        this.column3 = column3;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.dateTime = dateTime;
  }

  public DashboardEntity toEntity() {
    return new DashboardEntity(id, dashboardCode, column1, column2, column3, value1, value2, value3,
        dateTime);
  }
}
