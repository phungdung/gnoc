package com.viettel.gnoc.kedb.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.kedb.dto.KedbActionLogsDTO;
import java.io.Serializable;
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
 * @author tiennv
 * @version 1.0
 * @since 17/04/2018
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "ONE_TM", name = "KEDB_ACTION_LOGS")
public class KedbActionLogsEntity implements Serializable {

  static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "KEDB_ACTION_LOGS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "CREATE_TIME")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date createTime;

  @Column(name = "CREATE_UNIT_NAME")
  private String createUnitName;

  @Column(name = "CREATE_USER_NAME")
  private String createUserName;

  @Column(name = "KEDB_ID")
  private Long kedbId;

  @Column(name = "STATUS")
  private String status;

  public KedbActionLogsDTO toDTO() {
    KedbActionLogsDTO dto = new KedbActionLogsDTO(
        id,
        content,
        createTime,
        createUnitName,
        createUserName,
        kedbId,
        status
    );
    return dto;
  }
}
