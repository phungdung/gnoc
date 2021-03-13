package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.MethodParameterEntity;
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
public class MethodParameterDTO extends BaseDto {

  private Long methodParameterId;
  private Long dateType;
  private Long webserviceMethodId;
  private String parameterName;

  public MethodParameterEntity toEntity() {
    return new MethodParameterEntity(
        methodParameterId,
        dateType,
        webserviceMethodId,
        parameterName
    );
  }
}
