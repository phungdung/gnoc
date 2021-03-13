package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
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

@Entity
@Table(schema = "COMMON_GNOC", name = "LOG_CHANGE_CONFIG")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogChangeConfigEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "COMMON_GNOC.LOG_CHANGE_CONFIG_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = true)
  private Long id;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATE_TIME", nullable = true)
  private Date updateTime;
  @Column(name = "USER_CHANGE", nullable = true)
  private String userChange;
  @Column(name = "FUNCTION_CODE", nullable = true)
  private String functionCode;
  @Column(name = "CONTENT", nullable = true)
  private String content;
  @Column(name = "REMOTE_ADDR", nullable = true)
  private String remoteAddr;
  @Column(name = "REMOTE_HOST", nullable = true)
  private String remoteHost;
  @Column(name = "REQ_HEADER", nullable = true)
  private String reqHeader;

  public LogChangeConfigDTO toDTO() {
    LogChangeConfigDTO dto = new LogChangeConfigDTO(
        id,
        updateTime,
        userChange,
        functionCode,
        content, remoteAddr, remoteHost, reqHeader
    );
    return dto;
  }
}
