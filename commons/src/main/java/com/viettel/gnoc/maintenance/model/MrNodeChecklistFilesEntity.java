package com.viettel.gnoc.maintenance.model;


import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistFilesDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * @author tripm
 * @version 2.0
 * @since 12/06/2020 09:31:00
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "OPEN_PM", name = "MR_NODE_CHECKLIST_FILES")
public class MrNodeChecklistFilesEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OPEN_PM.MR_NODE_CHECKLIST_FILES_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "FILE_ID", unique = true, nullable = false)
  private Long fileId;

  @Column(name = "FILE_NAME")
  private String fileName;

  @Column(name = "FILE_PATH")
  private String filePath;

  @Column(name = "NODE_CHECKLIST_ID")
  private Long nodeChecklistId;

  @Column(name = "CREATED_USER")
  private String createdUser;

  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @Column(name = "UPDATED_USER")
  private String updatedUser;

  @Column(name = "UPDATED_TIME")
  private Date updatedTime;

  public MrNodeChecklistFilesDTO toDTO() {
    MrNodeChecklistFilesDTO dto = new MrNodeChecklistFilesDTO(
        fileId == null ? null : fileId.toString(),
        fileName,
        filePath,
        nodeChecklistId == null ? null : nodeChecklistId.toString(),
        createdUser,
        createdTime == null ? null : DateTimeUtils.convertDateToString(createdTime),
        updatedUser,
        updatedTime == null ? null : DateTimeUtils.convertDateToString(updatedTime)
    );
    return dto;
  }
}
