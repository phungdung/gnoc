package com.viettel.gnoc.maintenance.model;


import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisFileDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_SCHEDULE_BTS_HIS_FILE")
public class MrScheduleBtsHisFileEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_SCHEDULE_HIS_BTS_FILE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID_FILE", unique = true, nullable = false)
  private Long idFile;

  @Column(name = "WO_ID", nullable = false)
  private String woId;

  @Column(name = "FILE_NAME")
  private String fileName;

  @Column(name = "FILE_PATH")
  private String filePath;

  @Column(name = "USER_UPDATE")
  private String userUpdate;

  @Column(name = "CHECK_LIST_ID")
  private String checklistId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATED_DATE")
  private Date createdDate;

  public MrScheduleBtsHisFileEntity(String woId, String fileName, String filePath,
                                    String userUpdate, Long idFile, String checklistId, Date createdDate) {
    this.woId = woId;
    this.fileName = fileName;
    this.filePath = filePath;
    this.userUpdate = userUpdate;
    this.idFile = idFile;
    this.checklistId = checklistId;
    this.createdDate = createdDate;
  }

  public MrScheduleBtsHisFileDTO toDTO() {
    MrScheduleBtsHisFileDTO dto = new MrScheduleBtsHisFileDTO(
        woId,
        fileName,
        filePath,
        userUpdate,
        idFile == null ? null : idFile.toString(),
        checklistId,
        createdDate == null ? null : DateTimeUtils.convertDateToString(createdDate, DateTimeUtils.patternDateTimeMs)
    );
    return dto;
  }
}
