package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.LogChangeConfigEntity;
import java.util.Date;
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
public class LogChangeConfigDTO extends BaseDto {

  private Long id;
  private Date updateTime;
  private String userChange;
  private String functionCode;
  private String content;
  private String remoteAddr;
  private String remoteHost;
  private String reqHeader;

  private Object ObjNew;
  private Object ObjOld;

  public LogChangeConfigDTO(String userChange, String functionCode, String content,
      Object objNew, Object objOld) {
    this.userChange = userChange;
    this.functionCode = functionCode;
    this.content = content;
    ObjNew = objNew;
    ObjOld = objOld;
  }

  public LogChangeConfigDTO(Long id, Date updateTime, String userChange,
      String functionCode, String content, String remoteAddr, String remoteHost,
      String reqHeader) {
    this.id = id;
    this.updateTime = updateTime;
    this.userChange = userChange;
    this.functionCode = functionCode;
    this.content = content;
    this.remoteAddr = remoteAddr;
    this.remoteHost = remoteHost;
    this.reqHeader = reqHeader;
  }

  public LogChangeConfigEntity toEntity() {
    LogChangeConfigEntity model = new LogChangeConfigEntity(
        id,
        updateTime,
        userChange,
        functionCode,
        content,
        remoteAddr,
        remoteHost,
        reqHeader
    );
    return model;
  }
}
