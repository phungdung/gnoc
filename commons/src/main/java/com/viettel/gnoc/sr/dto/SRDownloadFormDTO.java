package com.viettel.gnoc.sr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.actuate.endpoint.web.Link;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SRDownloadFormDTO {

  private String country;
  private String countryName;
  private String serviceArray;
  private String serviceArrayName;
  private String serviceGroup;
  private String serviceGroupName;
  private String serviceId;
  private String serviceCode;
  private String serviceName;
  private Link attachFile;
  private String fileName;
  private String filePath;
  private String templateId;
}
