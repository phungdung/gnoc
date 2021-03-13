package com.viettel.gnoc.cr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class V_WebserviceMethodDetailDTO {

  private Long webserviceMethodId;
  private Long webserviceId;
  private Long sucessReturnValue;
  private String methodName;
  private String webserviceName;
  private String url;
  private String namespaceUri;
  private String localpart;
  private String webserviceClassPath;
  private String getPortMethod;
  //
  private String classPath;
  private String idField;
  private String returnValueField;
  private String userName;
  private String password;
  private String output;
}
