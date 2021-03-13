package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.TroublesIbmDTO;
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

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "ONE_TM", name = "TROUBLES_IBM")
public class TroublesIbmEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TROUBLES_IBM_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "IBM_NAME")
  private String ibmName;

  @Column(name = "RECEIVE_UNIT_CODE")
  private String receiveUnitCode;

  @Column(name = "RECEIVE_UNIT_NAME")
  private String receiveUnitName;

  @Column(name = "PRODUCT_NAME")
  private String productName;

  @Column(name = "PRODUCT_CODE")
  private String productCode;

  @Column(name = "RECEIVE_USER_NAME")
  private String receiveUserName;

  @Column(name = "CREATE_USER_ID")
  private Long createUserId;

  @Column(name = "CREATE_USER_NAME")
  private String createUserName;

  @Column(name = "IP_SERVER")
  private String ipServer;

  @Column(name = "ACCOUNT_ERROR")
  private String accountError;

  @Column(name = "ERROR_CODE")
  private String errorCode;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "ERROR_TIME")
  private Date errorTime;

  @Column(name = "NUM_COMPLAINT")
  private Long numComplaint;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "PROCESS")
  private String process;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "TIME_EXPECT_CLOSE")
  private String timeExpectClose;

  @Column(name = "TIME_CLOSE")
  private String timeClose;

  @Column(name = "UNIT_LIABLE")
  private String unitLiable;

  @Column(name = "USER_LIABLE")
  private String userLiable;

  @Column(name = "REASON")
  private String reason;

  @Column(name = "SOLUTION")
  private String solution;

  @Column(name = "TROUBLE_ID")
  private Long troubleId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @Column(name = "IS_MOVE_IBM")
  private Long isMoveIbm;

  @Column(name = "IBM_ID")
  private Long ibmId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "INSERT_IBM_TIME")
  private Date insertIbmTime;

  public TroublesIbmDTO toDTO() {
    return new TroublesIbmDTO(id, ibmName, receiveUnitCode, receiveUnitName, productName,
        productCode, receiveUserName, createUserId, createUserName, ipServer, accountError,
        errorCode, errorTime, numComplaint, description, process, status, timeExpectClose,
        timeClose, unitLiable, userLiable, reason, solution, troubleId, createdTime,
        isMoveIbm, ibmId, insertIbmTime, null);
  }
}
