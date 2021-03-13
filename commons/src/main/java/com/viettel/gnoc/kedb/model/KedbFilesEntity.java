package com.viettel.gnoc.kedb.model;

import com.viettel.gnoc.kedb.dto.KedbFilesDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(schema = "ONE_TM", name = "KEDB_FILES")
public class KedbFilesEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "KEDB_FILES_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
  @Column(name = "KEDB_FILE_ID", unique = true, nullable = false)
  private Long kedbFileId;
  @Column(name = "KEDB_FILE_NAME")
  private String kedbFileName;
  @Column(name = "CREATE_UNIT_ID")
  private Long createUnitId;
  @Column(name = "CREATE_UNIT_NAME")
  private String createUnitName;
  @Column(name = "CREATE_USER_ID")
  private Long createUserId;
  @Column(name = "CREATE_USER_NAME")
  private String createUserName;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATE_TIME")
  private Date createTime;
  @Column(name = "KEDB_ID")
  private Long kedbId;
  @Column(name = "CONTENT")
  private String content;

  public KedbFilesDTO toDTO() {
    return new KedbFilesDTO(kedbFileId, kedbFileName, createUnitId, createUnitName, createUserId,
        createUserName, createTime, kedbId, content);
  }
}
