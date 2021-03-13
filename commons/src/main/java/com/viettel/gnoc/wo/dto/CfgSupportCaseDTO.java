package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.wo.model.CfgSupportCaseEntity;
import java.util.List;
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
@MultiFieldUnique(message = "{validation.cfgSupportCase.null.unique}", clazz = CfgSupportCaseEntity.class, uniqueFields = "caseName,serviceID,infraTypeID", idField = "id")
public class CfgSupportCaseDTO extends BaseDto {

  private Long id;
  @NotEmpty(message = "{validation.cfgSupportCase.null.caseName}")
  private String caseName;

  @NotNull(message = "{validation.cfgSupportCase.null.serviceName}")
  private Long serviceID;

  @NotNull(message = "{validation.cfgSupportCase.null.infraType}")
  private Long infraTypeID;
  private String serviceName;
  private String infraTypeName;
  private String cfgSupportCaseID;
  private String status;
  private String testCaseName;
  private String fileRequiredTxt;
  List<CfgSupportCaseTestDTO> cfgSupportCaseTestListDTO;

  public CfgSupportCaseDTO(Long id, String caseName, Long serviceID, Long infraTypeID) {
    this.id = id;
    this.caseName = caseName;
    this.serviceID = serviceID;
    this.infraTypeID = infraTypeID;
  }

  public CfgSupportCaseEntity toEntity() {
    return new CfgSupportCaseEntity(id, caseName, serviceID, infraTypeID);
  }


}
