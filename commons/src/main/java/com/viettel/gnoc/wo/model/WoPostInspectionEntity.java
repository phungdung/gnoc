package com.viettel.gnoc.wo.model;

import com.viettel.gnoc.wo.dto.WoPostInspectionInsideDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ITSOL
 */

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "WO_POST_INSPECTION")
public class WoPostInspectionEntity {

  //Fields
  @Id
  @Column(name = "ID")
  private Long id;
  @Column(name = "WO_ID")
  private Long woId;
  @Column(name = "ACCOUNT")
  private String account;
  @Column(name = "POINT")
  private Long point;
  @Column(name = "NOTE")
  private String note;
  @Column(name = "CHECK_USER_NAME")
  private String checkUserName;
  @Column(name = "RECEIVE_USER_NAME")
  private String receiveUserName;
  @Column(name = "RESULT")
  private String result;
  @Column(name = "CREATED_TIME")
  private Date createdTime;
  @Column(name = "WO_CODE_PIN")
  private String woCodePin;
  @Column(name = "FILE_NAME")
  private String filename;
  @Column(name = "DATA_JSON")
  private String dataJson;
  @Column(name = "CD_ID")
  private Long cdId;
  @Column(name = "LOCATION_NAME")
  private String locationName;
  @Column(name = "ACCOUNT_WO_ID")
  private Long accountWoId;

  public WoPostInspectionInsideDTO toDTO() {
    return new WoPostInspectionInsideDTO(id, woId, account, point, note, checkUserName,
        receiveUserName, result, createdTime, woCodePin, filename, dataJson, cdId, locationName,
        accountWoId);
  }


}
