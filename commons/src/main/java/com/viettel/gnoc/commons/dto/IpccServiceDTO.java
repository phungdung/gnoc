/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.IpccServiceEntity;
import com.viettel.gnoc.commons.validator.Unique;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Unique(message = "{validation.IpccServiceDTO.null.unique}", clazz = IpccServiceEntity.class, uniqueField = "ipccServiceCode", idField = "ipccServiceId")
public class IpccServiceDTO extends BaseDto {

  private Long ipccServiceId;
  @NotEmpty(message = "{validation.IpccServiceDTO.null.ipccServiceCode}")
  private String ipccServiceCode;
  @NotEmpty(message = "{validation.IpccServiceDTO.null.url}")
  private String url;
  @NotEmpty(message = "{validation.IpccServiceDTO.null.function}")
  private String function;
  private Long isDefault;
  @NotEmpty(message = "{validation.IpccServiceDTO.null.userName}")
  private String userName;
  @NotEmpty(message = "{validation.IpccServiceDTO.null.password}")
  private String password;
  private String mobile;
  private String isDefaultStr;

  public IpccServiceDTO(Long ipccServiceId, String ipccServiceCode, String url, String function,
      Long isDefault, String userName, String password) {
    this.ipccServiceId = ipccServiceId;
    this.ipccServiceCode = ipccServiceCode;
    this.url = url;
    this.function = function;
    this.isDefault = isDefault;
    this.userName = userName;
    this.password = password;
  }

  public IpccServiceEntity toEntity() {
    IpccServiceEntity entity = new IpccServiceEntity(ipccServiceId, ipccServiceCode, url,
        function, isDefault, userName, password);
    return entity;
  }
}
