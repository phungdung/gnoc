package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.wfm.dto.LogCallIpccDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ITSOL
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LOG_CALL_IPCC")
public class LogCallIpccEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "log_call_ipcc_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "START_CALL_TIME")
  private Date startCallTime;
  @Column(name = "RESULT_TIME")
  private Date resultTime;
  @Column(name = "WO_ID")
  private Long woId;
  @Column(name = "TRANSACTION_ID")
  private String transactionId;
  @Column(name = "USER_NAME")
  private String userName;
  @Column(name = "PHONE")
  private String phone;
  @Column(name = "RECORD_FILE_CODE")
  private String recordFileCode;
  @Column(name = "RESULT")
  private String result;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "USER_CALL")
  private String userCall;


  public LogCallIpccDTO toDTO() {
    LogCallIpccDTO dto = new LogCallIpccDTO(
        id == null ? null : String.valueOf(id),
        startCallTime == null ? null
            : DateTimeUtils.convertDateToString(startCallTime, Constants.ddMMyyyyHHmmss),
        resultTime == null ? null
            : DateTimeUtils.convertDateToString(resultTime, Constants.ddMMyyyyHHmmss),
        woId == null ? null : String.valueOf(woId),
        transactionId,
        userName,
        phone,
        recordFileCode,
        result,
        description,
        userCall
    );
    return dto;
  }
}
