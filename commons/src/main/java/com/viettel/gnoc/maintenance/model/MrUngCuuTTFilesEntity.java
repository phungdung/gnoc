package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrUngCuuTTFilesDTO;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_UCTT_FILES")
public class MrUngCuuTTFilesEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OPEN_PM.MR_UCTT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "FILE_ID", unique = true, nullable = false)
  private Long fileId;
  @Column(name = "FILE_NAME", nullable = false)
  private String fileName;
  @Column(name = "FILE_PATH", nullable = false)
  private String filePath;
  @Column(name = "UCTT_ID", nullable = false)
  private Long ucttId;
  @Column(name = "CREATED_USER", nullable = false)
  private String createdUser;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME", nullable = false)
  private Date createdTime;
  @Column(name = "UPDATED_USER", nullable = false)
  private String updatedUser;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATED_TIME", nullable = false)
  private Date updatedTime;

  public MrUngCuuTTFilesDTO toDTO() {
    MrUngCuuTTFilesDTO dto = new MrUngCuuTTFilesDTO(
        fileId == null ? null : fileId.toString(),
        fileName,
        filePath,
        ucttId == null ? null : ucttId.toString(),
        createdUser,
        createdTime,
        updatedUser,
        updatedTime
    );
    return dto;
  }
}
