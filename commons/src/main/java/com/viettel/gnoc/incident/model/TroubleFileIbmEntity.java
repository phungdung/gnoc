package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.TroubleFileIbmDTO;
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

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "ONE_TM", name = "TROUBLE_FILE_IBM")
public class TroubleFileIbmEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TROUBLE_FILE_IBM_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "FILE_ID", unique = true, nullable = false)
  private Long fileId;

  @Column(name = "FILE_NAME", nullable = false)
  private String fileName;

  @Column(name = "PATH", nullable = false)
  private String path;

  @Column(name = "CREATE_TIME", nullable = false)
  private Date createTime;

  @Column(name = "CREATE_USER_ID")
  private Long createUserId;

  @Column(name = "TROUBLE_ID_IBM", nullable = false)
  private Long troubleIdIbm;

  public TroubleFileIbmDTO toDTO() {
    return new TroubleFileIbmDTO(fileId, fileName, path, createTime, createUserId, troubleIdIbm);
  }

}
