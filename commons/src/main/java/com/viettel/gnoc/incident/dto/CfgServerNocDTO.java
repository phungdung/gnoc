package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.incident.model.CfgServerNocEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class CfgServerNocDTO {

  //Fields
  private Long cfgServerNocId;
  private String link;
  private String linkName;
  private String serverName;
  private String userName;
  private String pass;
  private String salt;
  private String insertSource;

  public CfgServerNocDTO(Long cfgServerNocId, String link, String linkName, String serverName,
      String userName, String pass, String salt, String insertSource) {
    this.cfgServerNocId = cfgServerNocId;
    this.link = link;
    this.linkName = linkName;
    this.serverName = serverName;
    this.userName = userName;
    this.pass = pass;
    this.salt = salt;
    this.insertSource = insertSource;
  }

  public CfgServerNocEntity toEntity() {
    CfgServerNocEntity model = new CfgServerNocEntity(
        cfgServerNocId,
        link,
        linkName,
        serverName,
        userName,
        pass,
        salt,
        insertSource
    );
    return model;
  }

}
