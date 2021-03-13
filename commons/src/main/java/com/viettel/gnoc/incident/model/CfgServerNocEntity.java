package com.viettel.gnoc.incident.model;

import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "ONE_TM", name = "CFG_SERVER_NOC")
public class CfgServerNocEntity {

  //Fields
  @Id
  @Column(name = "CFG_SERVER_NOC_ID")
  private Long cfgServerNocId;

  @Column(name = "LINK")
  private String link;

  @Column(name = "LINK_NAME")
  private String linkName;

  @Column(name = "SERVER_NAME")
  private String serverName;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "PASS")
  private String pass;

  @Column(name = "SALT")
  private String salt;

  @Column(name = "INSERT_SOURCE")
  private String insertSource;

  public CfgServerNocEntity(Long cfgServerNocId, String link, String linkName, String serverName,
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

  public CfgServerNocDTO toDTO() {
    CfgServerNocDTO dto = new CfgServerNocDTO(
        cfgServerNocId,
        link,
        linkName,
        serverName,
        userName,
        pass,
        salt,
        insertSource
    );
    return dto;
  }
}
