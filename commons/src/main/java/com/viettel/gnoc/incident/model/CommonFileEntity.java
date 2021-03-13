/**
 * @(#)CommonFileBO.java 9/3/2015 10:36 AM, Copyright 2011 Viettel Telecom. All rights reserved
 * VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.CommonFileDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "ONE_TM", name = "COMMON_FILE")
public class CommonFileEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "ONE_TM.COMMON_FILE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "FILE_ID", unique = true, nullable = false)
  private Long fileId;

  @Column(name = "FILE_NAME", nullable = false)
  private String fileName;

  @Column(name = "PATH", nullable = false)
  private String path;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATE_TIME", nullable = false)
  private Date createTime;

  @Column(name = "CREATE_USER_ID", nullable = false)
  private Long createUserId;

  public CommonFileEntity(
      Long fileId, String fileName, String path, Date createTime, Long createUserId) {
    this.fileId = fileId;
    this.fileName = fileName;
    this.path = path;
    this.createTime = createTime;
    this.createUserId = createUserId;
  }

  public CommonFileDTO toDTO() {
    CommonFileDTO dto = new CommonFileDTO(
        fileId,
        fileName, path, createTime,
        createUserId
    );
    return dto;
  }
}
