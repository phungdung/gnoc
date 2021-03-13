package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.maintenance.dto.MrFilesAttachDTO;
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
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Entity
@Table(schema = "OPEN_PM", name = "MR_FILES_ATTACH")
public class MrFilesAttachEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_FILES_ATTACH_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "FILE_ID", unique = true, nullable = false)
  private Long fileId;
  @Column(name = "FILE_NAME")
  private String fileName;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "TIME_ATTACK")
  private Date timeAttack;
  @Column(name = "USER_ID")
  private Long userId;
  @Column(name = "FILE_TYPE")
  private String fileType;
  @Column(name = "FILE_PATH")
  private String filePath;
  @Column(name = "MR_ID")
  private Long mrId;

  public MrFilesAttachDTO toDTO() {
    MrFilesAttachDTO dto = new MrFilesAttachDTO(
        fileId == null ? null : fileId.toString(), fileName, timeAttack == null ? null
        : DateTimeUtils.convertDateToString(timeAttack, "dd/MM/yyyy HH:mm:ss"),
        userId == null ? null : userId.toString(), fileType, mrId == null ? null : mrId.toString(),
        filePath, null
    );
    return dto;
  }
}
