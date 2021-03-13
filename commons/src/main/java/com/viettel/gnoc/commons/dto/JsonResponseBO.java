package com.viettel.gnoc.commons.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JsonResponseBO implements Serializable {

  private int status;
  private String detailError;
  private String errorCode;
  private String dataJson;
  private byte[] byteDataJson;
  private String receiverTime;
  private String sendTime;
  private int totalDataJson;

  public JsonResponseBO() {
    this.status = 0;
  }
}
