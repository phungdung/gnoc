package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.cr.model.WebServiceMethodEntity;
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
@Unique(message = "{validation.cr.webServiceMethod.isDupplicate.CR}", clazz = WebServiceMethodEntity.class, uniqueField = "methodName", idField = "webServiceMethodId")
public class WebServiceMethodDTO extends BaseDto {

  private Long webServiceMethodId;
  private Long webServiceId;
  private String methodName;
  private Long sucessReturnValue;
  private String classPath;
  private String idField;
  private String returnValueField;
  private Long isMerge;

  public WebServiceMethodEntity toEntity() {
    return new WebServiceMethodEntity(
        webServiceMethodId,
        webServiceId,
        methodName,
        sucessReturnValue,
        classPath,
        idField,
        returnValueField,
        isMerge
    );
  }
}
