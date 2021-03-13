package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.cr.model.EmployeeImpactEntity;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@MultiFieldUnique(message = "{validation.EmployeeImpactDTO.multiple.unique}", clazz = EmployeeImpactEntity.class,
    uniqueFields = "empUsername,empArray,empArrayChild,empLevel", idField = "idImpact")
public class EmployeeImpactDTO extends BaseDto {

  private Long idImpact;
  private Long empId;
  @NotEmpty(message = "{validation.employeeImpact.null.empUserName}")
  private String empUsername;
  @NotNull(message = "{validation.employeeImpact.null.empArray}")
  private Long empArray;
  @NotNull(message = "{validation.employeeImpact.null.empLevel}")
  private Long empLevel;
  private String updatedUser;
  private Date updatedTime;
  private Date updatedTimeFrom;
  private Date updatedTimeTo;
  private String empArrayChild;

  private String userName;
  private String unitName;
  private String unitCode;
  private String empArrayName;
  private String empArrayChildName;
  private String unitId;
  private Long statusUpdate;
  private Long idImpactSave;
  private String empLevelName;

  public EmployeeImpactDTO(Long idImpact, Long empId, String empUsername, Long empArray,
      Long empLevel, String updatedUser, Date updatedTime, String empArrayChild) {
    this.idImpact = idImpact;
    this.empId = empId;
    this.empUsername = empUsername;
    this.empArray = empArray;
    this.empLevel = empLevel;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
    this.empArrayChild = empArrayChild;
  }

  public EmployeeImpactEntity toEntity() {
    return new EmployeeImpactEntity(
        idImpact,
        empId,
        empUsername,
        empArray,
        empLevel,
        updatedUser,
        updatedTime,
        empArrayChild
    );
  }
}
