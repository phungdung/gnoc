/**
 * @(#)CrFilesAttachBO.java 9/24/2015 9:41 AM, Copyright 2011 Viettel Telecom. All rights reserved
 * VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
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
 * @author anhmv6
 * @version 1.0
 * @since 9/24/2015 9:41 AM
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "CR_FILES_ATTACH")
public class CrFilesAttachEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CR_FILES_ATTACH_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "FILE_ID", unique = true, nullable = false)
  private Long fileId;
  @Column(name = "TEMP_IMPORT_ID")
  private Long tempImportId;
  @Column(name = "FILE_NAME")
  private String fileName;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "TIME_ATTACK")
  private Date timeAttack;
  @Column(name = "USER_ID")
  private Long userId;
  @Column(name = "FILE_TYPE")
  private String fileType;
  @Column(name = "CR_ID")
  private Long crId;
  @Column(name = "FILE_PATH")
  private String filePath;
  @Column(name = "FILE_SIZE")
  private Long fileSize;
  @Column(name = "DT_CODE")
  private String dtCode;
  @Column(name = "DT_FILE_HISTORY")
  private String dtFileHistory;
  @Column(name = "IS_RUN")
  private Long isRun;

  public CrFilesAttachInsiteDTO toDTO() {
    return new CrFilesAttachInsiteDTO(fileId, tempImportId, fileName, timeAttack,
        userId, fileType, crId, filePath, fileSize, dtCode, dtFileHistory, isRun);
  }

  public CrFilesAttachDTO toOutSideDTO() {
    CrFilesAttachDTO dto = new CrFilesAttachDTO(
        fileId == null ? null : fileId.toString(),
        tempImportId == null ? null : tempImportId.toString(),
        fileName,
        timeAttack == null ? null : DateTimeUtils.convertDateToString(timeAttack),
        userId == null ? null : userId.toString(),
        fileType,
        crId == null ? null : crId.toString(),
        filePath,
        fileSize == null ? null : fileSize.toString(),
        dtCode,
        dtFileHistory,
        null,
        isRun == null ? null : isRun.toString()
    );
    return dto;
  }
}

