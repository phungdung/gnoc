/**
 * @(#)RolesBO.java 8/27/2015 5:34 PM, Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.CfgWebServiceSQLDTO;
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
 * @author tuanpv14
 * @version 1.0
 * @since 8/27/2015 5:34 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "CFG_WEB_SERVICE_SQL")
public class CfgWebServiceSQLEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CFG_WEB_SERVICE_SQL_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true)
  private Long id;

  @Column(name = "CODE", nullable = false)
  private String code;

  @Column(name = "SQL_TEXT")
  private String sqlText;

  @Column(name = "SYSTEM", nullable = false)
  private String system;

  @Column(name = "STATUS")
  private Long status;

  @Column(name = "CREATE_TIME", nullable = false)
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date createTime;

  @Column(name = "UPDATE_TIME")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date updateTime;

  @Column(name = "FORMAT_DATE")
  private String formatDate;

  @Column(name = "WRITE_LOG")
  private Long writeLog;

  @Column(name = "COLUMN_KEY")
  private String columnKey;


  public CfgWebServiceSQLDTO toDTO() {
    CfgWebServiceSQLDTO dto = new CfgWebServiceSQLDTO(
        this.id,
        this.code,
        this.sqlText,
        this.system,
        this.status,
        this.createTime,
        this.updateTime,
        this.formatDate,
        this.writeLog,
        this.columnKey
    );
    return dto;
  }
}

