package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.sr.model.SRFlowExecuteEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SRFlowExecuteDTO extends BaseDto {

  //Fields
  private Long flowId;
  private String flowName;
  private String flowDescription;
  private Long country;

  private String listflowId;
  private String listCountry;
  private String countryName;
  private String listCountryName;
  private String stepNumber;
  private List<SRRoleActionDTO> lstRoleActionDTO;
  private List<SRFlowExecuteDTO> flowExecuteDTOMainList;
  private List<SRFlowExecuteDTO> flowExecuteDTODetailList;
  private int countStep;
  private Boolean btnDelete;

  public SRFlowExecuteDTO(Long flowId, String flowName, String flowDescription
      , Long country) {
    this.flowId = flowId;
    this.flowName = flowName;
    this.flowDescription = flowDescription;
    this.country = country;
  }

  public SRFlowExecuteEntity toEntity() {
    SRFlowExecuteEntity model = new SRFlowExecuteEntity(
        flowId
        , flowName
        , flowDescription
        , country
    );
    return model;
  }
}
