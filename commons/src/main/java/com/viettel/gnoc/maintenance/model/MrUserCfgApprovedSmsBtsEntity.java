package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.maintenance.dto.MrUserCfgApprovedSmsBtsDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author TrungDuong
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_USER_CFG_APPROVED_SMS_BTS")
public class MrUserCfgApprovedSmsBtsEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_CFG_APPROVED_SMS_BTS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")

  @Column(name = "USER_CFG_APPROVED_SMS_ID", unique = true, nullable = false)
  private Long userCfgApprovedSmsId;

  @Column(name = "MARKET_CODE")
  private String marketCode;

  @Column(name = "AREA_CODE")
  private String areaCode;

  @Column(name = "PROVINCE_CODE")
  private String provinceCode;

  @Column(name = "USERNAME")
  private String userName;

  @Column(name = "MOBILE")
  private String mobile;

  @Column(name = "APPROVE_LEVEL")
  private Long approveLevel;

  @Column(name = "FULLNAME")
  private String fullName;

  @Column(name = "USERS_ID")
  private Long userID;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATE_TIME")
  private Date createTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATE_TIME")
  private Date updateTime;

  @Column(name = "USER_UPDATE")
  private String userUpdate;

  @Column(name = "RECEIVE_MESSAGE_BD")
  private Long receiveMessageBD;


  public MrUserCfgApprovedSmsBtsDTO toDTO() {
    MrUserCfgApprovedSmsBtsDTO dto = new MrUserCfgApprovedSmsBtsDTO(
        userCfgApprovedSmsId,
        marketCode,
        areaCode,
        provinceCode,
        userName,
        mobile,
        approveLevel == null ? null : approveLevel.toString(),
        fullName,
        userID == null ? null : userID.toString(),
        createTime == null ? null
            : DateTimeUtils.convertDateToString(createTime, DateTimeUtils.patternDateTimeMs),
        updateTime == null ? null
            : DateTimeUtils.convertDateToString(updateTime, DateTimeUtils.patternDateTimeMs),
        userUpdate,
        receiveMessageBD == null ? null : receiveMessageBD.toString()
    );
    return dto;
  }
}
