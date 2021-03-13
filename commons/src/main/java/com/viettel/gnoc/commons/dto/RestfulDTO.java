/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.dto;

import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;

/**
 * @author thanhlv12
 */
@Getter
@Setter
@NoArgsConstructor
public class RestfulDTO {

  private String url;
  private String resourcePath;
  private HttpHeaders headers;
  private Map<String, String> params;
  private Map<String, String> mHeader;
  private String method;
  private Integer timeOut;
  private Boolean doOutPut;
  private String body;
  private String mediaType;

}
