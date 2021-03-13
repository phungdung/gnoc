package com.viettel.gnoc.commons.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestInputBO implements Serializable {

  private String countryCode;
  private String code;
  private List<ParameterBO> params;
  private String query;
  private int compressData;
}
