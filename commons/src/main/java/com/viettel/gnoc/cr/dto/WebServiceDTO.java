package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.WebServiceEntity;
import java.util.List;
import javax.validation.constraints.NotEmpty;
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

public class WebServiceDTO extends BaseDto {

  private Long webServiceId;
  private String webServiceName;
  @NotEmpty(message = "{validation.webService.null.url}")
  private String url;
  @NotEmpty(message = "{validation.webService.null.nameSpaceURI}")
  private String nameSpaceURI;
  @NotEmpty(message = "{validation.webService.null.localPart}")
  private String localPart;
  @NotEmpty(message = "{validation.webService.null.webServiceClassPath}")
  private String webServiceClassPath;
  @NotEmpty(message = "{validation.webService.null.getPortMethod}")
  private String getPortMethod;
  private String userName;
  private String password;
  private Long isActive;
  private Long isEditable;
  private List<WebServiceMethodDTO> webServiceMethodDTOS;

  public WebServiceDTO(Long webServiceId, String webServiceName, String url, String nameSpaceURI,
      String localPart, String webServiceClassPath, String getPortMethod, String userName,
      String password, Long isActive, Long isEditable) {
    this.webServiceId = webServiceId;
    this.webServiceName = webServiceName;
    this.url = url;
    this.nameSpaceURI = nameSpaceURI;
    this.localPart = localPart;
    this.webServiceClassPath = webServiceClassPath;
    this.getPortMethod = getPortMethod;
    this.userName = userName;
    this.password = password;
    this.isActive = isActive;
    this.isEditable = isEditable;
  }

  public WebServiceEntity toEntity() {
    return new WebServiceEntity(
        webServiceId,
        webServiceName,
        url,
        nameSpaceURI,
        localPart,
        webServiceClassPath,
        getPortMethod,
        userName,
        password,
        isActive,
        isEditable
    );
  }
}
